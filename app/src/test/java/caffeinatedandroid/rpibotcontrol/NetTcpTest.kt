package caffeinatedandroid.rpibotcontrol

import caffeinatedandroid.rpibotcontrol.net.http.TCP
import caffeinatedandroid.rpibotcontrol.net.http.TCP.Companion.defaultTcpPort
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.net.ServerSocket
import kotlin.concurrent.thread

class NetTcpTest {
    @Test
    fun tcpReportsNotConnectedYet() {
        val tcp = TCP("localhost")
        val isConnected = tcp.isConnected()
        assertThat(isConnected).isFalse()
    }

    @Test
    fun tcpSocketConnectsToServer() {
        // Simple test TCP server to test connections
        thread(start = true) {
            val server = ServerSocket(defaultTcpPort)
            server.soTimeout = 1000
            server.use { server ->
                server.accept()
            }
        }

        // Initialise TCP then connect to test server
        val tcp = TCP("localhost")
        var isConnected = false
        tcp.use {
            tcp.connect()
            isConnected = tcp.isConnected()
        }
        assertThat(isConnected).isTrue()
    }
}