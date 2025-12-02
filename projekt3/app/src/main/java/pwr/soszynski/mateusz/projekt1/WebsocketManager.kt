package pwr.soszynski.mateusz.projekt1

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class WebSocketManager {
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages

    suspend fun connect() {
        client.webSocket("wss://192.168.1.98:5001/ws") {
            try {
                // Send message
                send(Frame.Text("Hello Server!"))

                // Listen for incoming frames
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> _messages.emit(frame.readText())
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error: ${e.message}")
            }
        }
    }
}