package caffeinatedandroid.rpibotcontrol.net

import android.content.Context
import caffeinatedandroid.rpibotcontrol.net.bluetooth.BLE
import caffeinatedandroid.rpibotcontrol.net.http.TCP
import caffeinatedandroid.rpibotcontrol.net.http.UDP
import java.io.Closeable

interface IConnectionBase : Closeable {
    fun isConnected(): Boolean
    fun send(msgType: MessageType, msg: String): String?
}

interface IConnection : IConnectionBase {
    fun connect()
}

interface IConnectionManager : IConnectionBase {
    suspend fun connect(context: Context)
}

enum class MessageType {
    // System
    Discover,
    // Navigation (Direct)
    MoveForward,
    MoveBackward,
    MoveLeft,
    MoveRight,
    // Other
    Pause,
    Resume,
    // Power State
    Shutdown,
}

object Connection : IConnectionManager {
    private lateinit var conn: IConnection

    init {
        init()
    }

    private fun init() {
        // Determine which Net Connection type to use (check preferences)
//        if (true) {
//            // conn = BLE()
//            // conn = TCP()
//        }
    }

    fun resetConnectionType() {
        conn.close()
        init()
        conn.connect()
    }

    override suspend fun connect(context: Context) {
        // TEMP testing UDP discovery (will be internal to TCP connection, enabled with a Bool)
        UDP.discoverConnections(context)
    }

    override fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    override fun send(msgType: MessageType, msg: String): String? {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}