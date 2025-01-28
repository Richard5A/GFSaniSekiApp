package ui.map

import Main
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import data.MessageData
import data.datasources.places.PlaceDataSource
import dev.datlag.kcef.KCEF
import dev.datlag.kcef.KCEFBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import mapWindowOpenFlow
import ui.einsatzmelder.EinsatzmelderViewModel
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.math.max


@Composable
fun MapScreen(viewModel: EinsatzmelderViewModel, mapWebViewNavigator: WebViewNavigator) {
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
            println("Message Params: " + message.params)
            val data = Json.decodeFromString<MessageData>(message.params)
            when (data.type) {
                "data" -> {
                    viewModel.setLatLng(data.lat, data.lng, data.name)
                }

                "onload" -> {
                    val places = PlaceDataSource().getPlaces() ?: emptyList()
                    callback(dataToJsonString(places))
                }

                else -> println("Unknown message")
            }
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
                        mapWindowOpenFlow.value = false
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

    var htmlData = ""
    Main::class.java.getResourceAsStream("assets/map.html").use { inputStream ->
        if (inputStream != null) {
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
                    state = state, modifier = Modifier.fillMaxSize(), onCreated = {
                        println("Created WebView")
                    }, onDispose = { s ->
                        s.dispose()
                        println("Disposed")
                    }, webViewJsBridge = jsBridge,
                    navigator = mapWebViewNavigator
                )
            } else {
                Text(text = "Downloading $downloading%")
            }
        }

    }
}