package caffeinatedandroid.rpibotcontrol

import caffeinatedandroid.rpibotcontrol.net.MessageType
import caffeinatedandroid.rpibotcontrol.net.http.TCP
import com.google.common.truth.Truth.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.concurrent.thread

class NetTcpTest {

    companion object {
        lateinit var server: ServerSocket

        @BeforeClass
        @JvmStatic
        fun setupServerSocket() {
            // Simple test TCP server to test connections
            server = ServerSocket(TCP.defaultPort)
            server.soTimeout = 5000
            thread(start = true) {
                server.use { server ->
                    while (server.isBound) {
                        val socket = server.accept()
                        thread(start = true) {
                            val out = PrintWriter(socket.getOutputStream(), true)
                            val `in` = BufferedReader(
                                InputStreamReader(socket.getInputStream())
                            )
                            // Simply repeat received message back to client
                            var inputLine: String
                            while (`in`.readLine().also { inputLine = it } != null) {
                                out.println(inputLine)
                                if (inputLine == "close") break
                            }
                        }
                    }
                }
            }
        }

        @AfterClass
        @JvmStatic
        fun cleanupServerSocket() {
            server.close()
        }
    }

    @Test
    fun tcpDoesNotConnectAutomatically() {
        val tcp = TCP("localhost")
        val isConnected = tcp.isConnected()
        assertThat(isConnected).isFalse()
    }

    @Test
    fun tcpSocketConnectsToServer() {
        // Initialise TCP then connect to test server
        val tcp = TCP("localhost")
        var isConnected = false
        tcp.use {
            tcp.connect()
            isConnected = tcp.isConnected()
        }
        assertThat(isConnected).isTrue()
    }

    @Test
    fun sendMessage() {
        val tcp = TCP("localhost")
        tcp.use {
            tcp.connect()
            tcp.send(MessageType.MoveForward, "forward")
        }
    }

    @Test
    fun sendMessage_checkResponse() {
        val tcp = TCP("localhost")
        val message = "forward"
        var response: String? = "default"
        tcp.use {
            tcp.connect()
            response = tcp.send(MessageType.MoveForward, message)
        }
        assertThat(response).isEqualTo(message)
    }

    @Test
    fun closeConnectionBeforeConnected() {
        val tcp = TCP("localhost")
        tcp.close()
    }
}