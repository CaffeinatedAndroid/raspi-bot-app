package caffeinatedandroid.rpibotcontrol.net.bluetooth

import caffeinatedandroid.rpibotcontrol.net.IConnection
import caffeinatedandroid.rpibotcontrol.net.MessageType

class BLE : IConnection {
    override fun send(msgType: MessageType, msg: String): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connect() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isConnected(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}