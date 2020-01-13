package caffeinatedandroid.rpibotcontrol.net.http

import android.content.Context
import android.net.wifi.WifiManager
import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


class UDP(private val host: String, private val port: Int = 51927, private val broadcast_port: Int = 51928) : IConnection {

    private lateinit var udpSocket: DatagramSocket
    private val serverAddr: InetAddress = InetAddress.getByName(host)

    init {
        connect()
    }

    override fun connect() {
        udpSocket = DatagramSocket(port)
    }

    override fun send(msgType: MessageType, msg: String): String {
        val buf = msg.toByteArray()
        val packet = DatagramPacket(buf, buf.size, serverAddr, port)
        udpSocket.send(packet)
        return ""
    }

    override fun close() {
        udpSocket.close()
    }

    fun discoverConnections(context: Context) {
        val msg = "DISCOVER_RPIBOT"
        val socket = DatagramSocket(broadcast_port)
        socket.broadcast = true
        val packet = DatagramPacket(msg.toByteArray(), msg.length, getBroadcastAddress(context), port)
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