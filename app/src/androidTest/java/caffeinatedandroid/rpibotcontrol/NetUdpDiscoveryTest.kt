package caffeinatedandroid.rpibotcontrol

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import caffeinatedandroid.rpibotcontrol.net.http.TCP
import caffeinatedandroid.rpibotcontrol.net.http.UDP
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.BeforeClass
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import kotlin.concurrent.thread

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NetUdpDiscoveryTest {

    companion object {
        // Context of the app under test.
        lateinit var context: Context

        @BeforeClass
        @JvmStatic
        fun setupServerSocket() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            UDP.serverBroadcastPort = UDP.serverBroadcastPort + 100
        }
    }

    @Test
    fun correctAppContext() {
        assertEquals("caffeinatedandroid.rpibotcontrol", context.packageName)
    }

    @Test
    fun discoveryBroadcastSent() {
        GlobalScope.launch {
            UDP.discoverConnections(context)
        }
    }
}
