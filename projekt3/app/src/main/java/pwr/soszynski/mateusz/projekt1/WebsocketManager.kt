package pwr.soszynski.mateusz.projekt1

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WebSocketManager {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    private var sender: (suspend (text: String) -> Unit)? = null

    suspend fun sendStr(msg: String) {
        if (sender != null) {
            sender!!(msg)
        }
    }

    suspend fun connect(url: String) {
        client.webSocket(url) {
            try {
                // Send message
                sender = { send(Frame.Text(it)) }

                // Listen for incoming frames
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            Log.d("MSG", frame.readText())
                            _messages.emit(frame.readText())
                        }

                        else -> {}
                    }
                }
                sender = null
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            }
        }
    }
}