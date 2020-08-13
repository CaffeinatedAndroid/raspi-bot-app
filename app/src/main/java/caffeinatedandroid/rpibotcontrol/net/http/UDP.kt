package caffeinatedandroid.rpibotcontrol.net.http

import android.content.Context
import android.net.wifi.WifiManager
import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * A UDP network connection.
 */
class UDP(host: String, private val port: Int = defaultPort) : IConnection {

    companion object {

        /**
         * The default UDP port number.
         */
        const val defaultPort = 9021
        private const val defaultBroadcastPort = 9022
        private const val defaultBroadcastTcpResponsePort = 9023

        /**
         * Copy of the default port. Used for 'localhost' testing, when the local and remote
         * port numbers would clash.
         */
        var serverPort = defaultPort

        /**
         * Copy of the broadcast port. Used for 'localhost' testing, when the local and remote
         * port numbers would clash.
         */
        var serverBroadcastPort = defaultBroadcastPort

        /**
         * Discover available devices that can connect via UDP
         */
        fun discoverConnections(
            context: Context,
            broadcast_port: Int = defaultBroadcastPort
        ) {
            val msg = "DISCOVER_RPIBOT_ADDR"
            val socket = DatagramSocket(broadcast_port)
            socket.broadcast = true
            val packet = DatagramPacket(
                msg.toByteArray(),
                msg.length,
                getBroadcastAddress(context),
                serverBroadcastPort
            )
            socket.send(packet)

            // TODO Listen for responses
        }

        /**
         * Gets the broadcast [InetAddress] from the [WifiManager]
         */
        @Throws(IOException::class)
        private fun getBroadcastAddress(context: Context): InetAddress? {
            val wifi: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val dhcp = wifi.dhcpInfo
            val broadcast = dhcp.ipAddress and dhcp.netmask or dhcp.netmask.inv()
            val quads = ByteArray(4)
            for (k in 0..3) quads[k] = (broadcast shr k * 8 and 0xFF).toByte()
            return InetAddress.getByAddress(quads)
        }
    }

    private var udpSocket: DatagramSocket? = null
    private val serverAddress: InetAddress = InetAddress.getByName(host)

    /**
     * Binds the [DatagramSocket] to a port.
     */
    override fun connect() {
        udpSocket = DatagramSocket(port)
    }

    /**
     * Determines if the [DatagramSocket] is bound.
     * Unlike other [IConnection], UDP does not connect (like TCP), but ports
     * are reserved and bound to a [DatagramSocket].
     */
    override fun isConnected(): Boolean {
        return udpSocket?.isBound ?: false
    }

    /**
     * Sends a message to the connected device.
     */
    override fun send(msgType: MessageType, msg: String): String? {
        if (udpSocket == null) {
            throw IOException("DatagramSocket not bound. Connect first to bind to port.")
        }
        val buf = msg.toByteArray()
        val packet = DatagramPacket(buf, buf.size, serverAddress, port)
        udpSocket?.send(packet)
        return null
    }

    /**
     * Closes the connection to the device.
     * Unbinds the port from the [DatagramSocket]
     */
    override fun close() {
        udpSocket?.close()
    }
}