package caffeinatedandroid.rpibotcontrol.net.http

import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class TCP(private val host: String, private val port: Int = defaultPort) : IConnection {

    companion object {
        const val defaultPort = 9020
    }

    private var clientSocket: Socket? = null
    private lateinit var out: PrintWriter
    private lateinit var `in`: BufferedReader

    override fun connect() {
        clientSocket = Socket(host, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    override fun isConnected(): Boolean {
        return clientSocket?.isConnected ?: false
    }

    override fun send(msgType: MessageType, msg: String): String? {
        out.println(msg)
        return `in`.readLine()
    }

    override fun close() {
        if (this::`in`.isInitialized) {
            `in`.close()
        }
        if (this::out.isInitialized) {
            out.close()
        }
        clientSocket?.close()
    }
}