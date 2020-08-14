package caffeinatedandroid.rpibotcontrol

import caffeinatedandroid.rpibotcontrol.net.MessageType
import caffeinatedandroid.rpibotcontrol.net.http.UDP
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NetUdpTest {

    companion object {

        private lateinit var server: DatagramSocket

        @BeforeClass
        @JvmStatic
        fun setupServerSocket() {
            UDP.serverPort = UDP.serverPort + 100
            server = DatagramSocket(UDP.serverPort)
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
            udp.connect()
            udp.send(MessageType.MoveForward, "forward")
        }
    }

    @Test
    fun sentMessageReceived() {
        val msg = "forward"
        val buffer = ByteArray(16)
        val packet = DatagramPacket(
            buffer,
            buffer.size,
            InetAddress.getByName("localhost"),
            UDP.serverPort
        )
        val udp = UDP("localhost")
        udp.use {
            udp.connect()
            runBlocking {
                withContext(Dispatchers.IO) {
                    udp.send(MessageType.MoveForward, msg)
                    server.receive(packet)
                }
                val result = String(packet.data, 0, packet.length)
                assertThat(result.trim()).isEqualTo(msg.trim())
            }
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