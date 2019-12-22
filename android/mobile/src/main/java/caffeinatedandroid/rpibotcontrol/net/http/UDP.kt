package caffeinatedandroid.rpibotcontrol.net.http

import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class UDP(private val host: String, private val port: Int) : IConnection {

    private lateinit var udpSocket: DatagramSocket
    private val serverAddr: InetAddress = InetAddress.getByName(host)

    override fun connect() {
        udpSocket = DatagramSocket(port)
    }

    override fun disconnect() {
        udpSocket.close()
    }

    override fun send(msgType: MessageType, msg: String): String {
        val buf = msg.toByteArray()
        val packet = DatagramPacket(buf, buf.size, serverAddr, port)
        udpSocket.send(packet)
        return ""
    }
}