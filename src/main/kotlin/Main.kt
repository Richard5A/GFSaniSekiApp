import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.multiplatform.webview.web.rememberWebViewNavigator
import data.datasources.credentials.CredentialsDataSource
import it.sauronsoftware.junique.JUnique
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import ui.einsatzmelder.EinsatzmelderScreen
import ui.einsatzmelder.EinsatzmelderViewModel
import ui.login.LoginScreen
import ui.login.LoginViewModel
import ui.map.MapScreen
import ui.resources.Res
import ui.resources.logo
import ui.theme.EinsatzmelderTheme
import ui.windows.getTrayIcon
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import kotlin.system.exitProcess


const val APP_ID = "GymFreihamSaniSekiApp"
val mapWindowOpenFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)

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
            if (trayIcon.popupMenu.isEnabled) windowOpenFlow.value = true
        }

        override fun mousePressed(e: MouseEvent?) {}
        override fun mouseReleased(e: MouseEvent?) {}
        override fun mouseEntered(e: MouseEvent?) {}
        override fun mouseExited(e: MouseEvent?) {}
    })

    application {
        // Main Window
        val mapWebViewNavigator = rememberWebViewNavigator()
        val einsatzmelderViewModel = remember { EinsatzmelderViewModel(mapWebViewNavigator) }
        val credentials by CredentialsDataSource.credentialsFlow.collectAsState()
        val isWindowOpen by windowOpenFlow.collectAsState()
        val windowState = rememberWindowState(size = DpSize(450.dp, 700.dp))
        if (isWindowOpen) {
            Window(
                onCloseRequest = { windowOpenFlow.value = false; mapWindowOpenFlow.value = false },
                title = "Schulsanitätsdienst Einsatzmelder",
                icon = painterResource(Res.drawable.logo),
                state = windowState,
            ) {
                EinsatzmelderTheme {
                    Surface(color = MaterialTheme.colorScheme.surface) {
                        if (credentials != null) {
                            EinsatzmelderScreen(einsatzmelderViewModel)
                        } else {
                            LoginScreen(LoginViewModel())
                        }
                    }
                }
            }
        }

        val mapWindowState = rememberWindowState(position = WindowPosition(500.dp, 10.dp))
        val isMapWindowOpen by mapWindowOpenFlow.collectAsState()
        Window(
            onCloseRequest = { mapWindowOpenFlow.value = false },
            visible = isMapWindowOpen,
            state = mapWindowState,
            icon = painterResource(Res.drawable.logo),
            title = "Schulsanitätsdienst Einsatort Map",
            onKeyEvent = { keyEvent ->
                if (keyEvent.key == Key.Escape && keyEvent.type == KeyEventType.KeyDown) {
                    mapWindowOpenFlow.value = false
                    true
                } else {
                    false
                }
            }) { MapScreen(einsatzmelderViewModel, mapWebViewNavigator) }
    }
}

class Main

