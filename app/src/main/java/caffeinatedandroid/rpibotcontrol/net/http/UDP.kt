package caffeinatedandroid.rpibotcontrol.net.http

import android.content.Context
import android.net.wifi.WifiManager
import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class UDP(host: String, private val port: Int = defaultPort) : IConnection {

    companion object {

        const val defaultPort = 9021
        private const val defaultBroadcastPort = 9022
        private const val defaultBroadcastTcpResponsePort = 9023

        // Copies of local and server ports - they can be made different (e.g., for 'localhost' testing)
        var serverPort = defaultPort
        var serverBroadcastPort = defaultBroadcastPort

        fun discoverConnections(
            context: Context,
            broadcast_port: Int = defaultBroadcastPort
        ) {
            // TODO optional port/serverPort params
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

        @Throws(IOException::class)
        fun getBroadcastAddress(context: Context): InetAddress? {
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

    // Copies of local and server ports - they can be made different (e.g., for 'localhost' testing)
//    var serverPort = port
//    var serverBroadcastPort = broadcast_port

    override fun connect() {
        udpSocket = DatagramSocket(port)
    }

    override fun isConnected(): Boolean {
        return udpSocket?.isBound ?: false
    }

    override fun send(msgType: MessageType, msg: String): String? {
        if (udpSocket == null) {
            throw IOException("DatagramSocket not bound. Connect first to bind to port.")
        }
        val buf = msg.toByteArray()
        val packet = DatagramPacket(buf, buf.size, serverAddress, port)
        udpSocket?.send(packet)
        return null
    }

    override fun close() {
        udpSocket?.close()
    }

//    ///////////////////////
//    // Service Discovery //
//    ///////////////////////
//
//    // TODO refactor these out into their own Class `UDPDiscovery` which extends UDP (+IConnection) or just IConnection
//
//    fun discoverConnections(context: Context) {
//        val msg = "DISCOVER_RPIBOT"
//        val socket = DatagramSocket(broadcast_port)
//        socket.broadcast = true
//        val packet = DatagramPacket(
//            msg.toByteArray(),
//            msg.length,
//            getBroadcastAddress(context),
//            serverBroadcastPort
//        )
//        socket.send(packet)
//
//        // TODO Listen for responses
//    }
//
//    @Throws(IOException::class)
//    fun getBroadcastAddress(context: Context): InetAddress? {
//        val wifi: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        val dhcp = wifi.dhcpInfo
//        val broadcast = dhcp.ipAddress and dhcp.netmask or dhcp.netmask.inv()
//        val quads = ByteArray(4)
//        for (k in 0..3) quads[k] = (broadcast shr k * 8 and 0xFF).toByte()
//        return InetAddress.getByAddress(quads)
//    }
}