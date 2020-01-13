package caffeinatedandroid.rpibotcontrol.net.http

import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCP(private val host: String, private val port: Int) : IConnection {

    private lateinit var clientSocket: Socket
    private lateinit var out: PrintWriter
    private lateinit var `in`: BufferedReader

    override fun connect() {
        clientSocket = Socket(host, port)
        out = PrintWriter(clientSocket.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
    }

    override fun disconnect() {
        `in`.close()
        out.close()
        clientSocket.close()
    }

    override fun send(msgType: MessageType, msg: String): String {
        out.println(msg)
        return `in`.readLine()
    }
}