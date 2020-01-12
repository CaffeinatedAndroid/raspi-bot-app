package caffeinatedandroid.rpibotcontrol.net

import caffeinatedandroid.rpibotcontrol.net.bluetooth.BLE

interface IConnection {
    fun connect()
    fun disconnect()
    fun send(msgType: MessageType, msg: String): String
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

object Connection {
    private lateinit var conn: IConnection

    init {
        init()
    }

    private fun init() {
        // Determine which Net Connection type to use (check preferences)
        if (true) {
            conn = BLE()
        }
    }

    fun resetConnectionType() {
        conn.disconnect()
        init()
        conn.connect()
    }
}