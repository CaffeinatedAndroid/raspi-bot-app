package caffeinatedandroid.rpibotcontrol

import caffeinatedandroid.rpibotcontrol.net.http.TCP
import caffeinatedandroid.rpibotcontrol.net.http.TCP.Companion.defaultTcpPort
import com.google.common.truth.Truth.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.net.ServerSocket
import kotlin.concurrent.thread

class NetTcpTest {

    companion object {
        lateinit var server: ServerSocket

        @BeforeClass
        @JvmStatic
        fun setupServerSocket() {
            // Simple test TCP server to test connections
            server = ServerSocket(defaultTcpPort)
            server.soTimeout = 5000
            thread(start = true) {
                server.use { server ->
                    server.accept()
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
    fun tcpReportsNotConnectedYet() {
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
}