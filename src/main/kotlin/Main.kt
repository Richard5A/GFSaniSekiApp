import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.multiplatform.webview.jsbridge.*
import com.multiplatform.webview.web.*
import data.datasources.credentials.CredentialsDataSource
import dev.datlag.kcef.KCEF
import dev.datlag.kcef.KCEFBuilder
import it.sauronsoftware.junique.JUnique
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import ui.einsatzmelder.EinsatzmelderScreen
import ui.einsatzmelder.EinsatzmelderViewModel
import ui.login.LoginScreen
import ui.login.LoginViewModel
import ui.resources.Res
import ui.resources.logo
import ui.theme.EinsatzmelderTheme
import ui.windows.getTrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.math.max
import kotlin.system.exitProcess


const val APP_ID = "GymFreihamSaniSekiApp"

fun main() {
    println("Starting application")
    val windowOpenFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)

    // Check if this is the only application running, otherwise stopping this
    // and notify the other instance.
    var alreadyRunning: Boolean
    try {
        JUnique.acquireLock(APP_ID) { windowOpenFlow.value = true; null }
        alreadyRunning = false
    } catch (e: Exception) {
        alreadyRunning = true
    }
    if (alreadyRunning) {
        JUnique.sendMessage(APP_ID, "tried to open")
        println("Process already running, closing this one.")
        exitProcess(0)
    }

    // TRAY ICON INIT
    val trayIcon = getTrayIcon()
    // Action Listener only reacts on double click
    trayIcon.addMouseListener(object : MouseListener {
        override fun mouseClicked(e: MouseEvent?) {
            if (trayIcon.popupMenu.isEnabled)
                windowOpenFlow.value = true
        }

        override fun mousePressed(e: MouseEvent?) {}
        override fun mouseReleased(e: MouseEvent?) {}
        override fun mouseEntered(e: MouseEvent?) {}
        override fun mouseExited(e: MouseEvent?) {}
    })

    application {

        var restartRequired by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(0F) }
        var initialized by remember { mutableStateOf(false) }
        val download: KCEFBuilder.Download = remember { KCEFBuilder.Download.Builder().github().build() }
        val jsBridge = rememberWebViewJsBridge()

        val mapJsMessageHandler = object : IJsMessageHandler {

            override fun methodName(): String {
                return "Map"
            }
            override fun handle(message: JsMessage, navigator: WebViewNavigator?, callback: (String) -> Unit) {

            }

        }

        LaunchedEffect(jsBridge) {
            jsBridge.register(handler = mapJsMessageHandler)

        }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                KCEF.init(builder = {
                    installDir(File("kcef-bundle"))

                    progress {
                        onDownloading {
                            downloading = max(it, 0F)
                        }
                        onInitialized {
                            initialized = true
                        }
                    }
                    settings {
                        cachePath = File("cache").absolutePath
                    }
                }, onError = {
                    it?.printStackTrace()
                }, onRestartRequired = {
                    restartRequired = true
                })
            }
        }



        DisposableEffect(Unit) {
            onDispose {
                KCEF.disposeBlocking()
            }
        }

        // Main Window
        val credentials by CredentialsDataSource.credentialsFlow.collectAsState()
        val isWindowOpen by windowOpenFlow.collectAsState()
        val windowState = rememberWindowState(size = DpSize(450.dp, 700.dp))
        if (isWindowOpen) {
            Window(
                onCloseRequest = { windowOpenFlow.value = false },
                title = "Schulsanitätsdienst Einsatzmelder",
                icon = painterResource(Res.drawable.logo),
                state = windowState,
            ) {
                EinsatzmelderTheme {
                    Surface(color = MaterialTheme.colorScheme.surface) {
                        if (credentials != null) {
                            EinsatzmelderScreen(EinsatzmelderViewModel())
                        } else {
                            LoginScreen(LoginViewModel())
                        }
                    }
                }
            }
        }

        val mapWindowState = rememberWindowState()
        Window(onCloseRequest = ::exitApplication, state = mapWindowState) {
            var htmlData: String = ""
            Main::class.java.getResourceAsStream("assets/map.html")
                .use { inputStream ->
                    if(inputStream != null) {
                        BufferedReader(InputStreamReader(inputStream)).use { reader ->
                            htmlData = reader.readText()
                        }
                    } else {
                        println("inputStream is null")
                    }
                }
//            val htmlData = Main::class.java.getResource("assets/map.html").readText()
            val state = rememberWebViewStateWithHTMLData(htmlData)
            Column {
                // Text(state.loadingState.toString())
                if (restartRequired) {
                    Text(text = "Restart required.")
                } else {
                    if (initialized) {
                        WebView(
                            state = state,
                            modifier = Modifier.fillMaxSize(),
                            onCreated = {
                                println("Created WebView")
                            },
                            onDispose = { s ->
                                println("Disposed")
                            },
                            webViewJsBridge = jsBridge
                        )
                    } else {
                        Text(text = "Downloading $downloading%")
                    }
                }

            }
        }
    }
}

class Main

