package caffeinatedandroid.rpibotcontrol

import caffeinatedandroid.rpibotcontrol.net.MessageType
import caffeinatedandroid.rpibotcontrol.net.http.UDP
import com.google.common.truth.Truth.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import java.net.DatagramSocket

class NetUdpTest {

    companion object {

        lateinit var server: DatagramSocket

        // Use UDP_Port+100 to get an unused port for testing only
        private const val PORT_TESTING_OFFSET = 100
        private const val UDP_TEST_PORT = 51927 + PORT_TESTING_OFFSET // TODO get port from UDP Class

        @BeforeClass
        @JvmStatic
        fun setupServerSocket() {
            // Simple test UDP server to test connections
            server = DatagramSocket(UDP_TEST_PORT)
        }

        @AfterClass
        @JvmStatic
        fun cleanupServerSocket() {
            server.close()
        }
    }

    @Test
    fun udpDoesNotConnectAutomaticallyAndHandlesPrematureClose() {
        val udp = UDP("localhost")
        var isBound = false
        // Connections do not apply to UDP, despite the interface function names
        // Instead, UDP.connect and isConnected refers to the DatagramSocket's bound status.
        udp.use {
            // Check UDP does not connect automatically
            isBound = udp.isConnected()
        }
        assertThat(isBound).isFalse()
    }

    @Test
    fun udpSocketConnectsToPortSuccessfully() {
        val udp = UDP("localhost")
        var isBound = false
        // Connections do not apply to UDP, despite the interface function names
        // Instead, UDP.connect and isConnected refers to the DatagramSocket's bound status.
        udp.use {
            udp.connect()
            isBound = udp.isConnected()
        }
        assertThat(isBound).isTrue()
    }

    @Test
    fun sendMessage() {
        val udp = UDP("localhost")
        udp.use {
            udp.serverPort = udp.serverPort + PORT_TESTING_OFFSET
            udp.connect()
            udp.send(MessageType.MoveForward, "forward")
        }
    }

    /**
     * IOException should be thrown as the DatagramSocket is not bound before attempting to
     * send data through it.
     */
    @Test(expected = IOException::class)
    fun sendMessageWhenNotBound() {
        val udp = UDP("localhost")
        udp.use {
            udp.send(MessageType.MoveForward, "forward")
        }
    }
}