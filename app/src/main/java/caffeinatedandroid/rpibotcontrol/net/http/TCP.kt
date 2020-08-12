package caffeinatedandroid.rpibotcontrol.net.http

import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

/**
 * A TCP network connection.
 */
class TCP(private val host: String, private val port: Int = defaultPort) : IConnection {

    companion object {
        /**
         * The default TCP port number.
         */
        const val defaultPort = 9020
    }

    private var clientSocket: Socket? = null
    private lateinit var out: PrintWriter
    private lateinit var `in`: BufferedReader

    /**
     * Connects to the remote socket
     */
    override fun connect() {
        clientSocket = Socket(host, port)
        out = PrintWriter(clientSocket!!.getOutputStream(), true)
        `in` = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
    }

    /**
     * Determines if this connection is connected.
     */
    override fun isConnected(): Boolean {
        return clientSocket?.isConnected ?: false
    }

    /**
     * Sends a message to the connected device.
     */
    override fun send(msgType: MessageType, msg: String): String? {
        out.println(msg)
        return `in`.readLine()
    }

    /**
     * Closes the connection to the device.
     */
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