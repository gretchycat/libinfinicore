package com.infinifolder.storage

import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkConnectionStatusTest {
    @Test
    fun unknownConnectionReportsConnectingUntilRequestCompletes() {
        val key = NetworkConnectionStatus.Key("test-provider", "initial-connect-${System.nanoTime()}")

        val request = NetworkConnectionStatus.begin(key)
        assertEquals(NetworkConnectionStatus.State.CONNECTING, NetworkConnectionStatus.state(key))

        NetworkConnectionStatus.succeeded(request)
        assertEquals(NetworkConnectionStatus.State.CONNECTED, NetworkConnectionStatus.state(key))
    }

    @Test
    fun failedConnectEndsConnectingState() {
        val key = NetworkConnectionStatus.Key("test-provider", "failed-connect-${System.nanoTime()}")

        val request = NetworkConnectionStatus.begin(key)
        assertEquals(NetworkConnectionStatus.State.CONNECTING, NetworkConnectionStatus.state(key))

        NetworkConnectionStatus.failed(request, java.net.SocketTimeoutException("timed out"))
        assertEquals(NetworkConnectionStatus.State.DISCONNECTED, NetworkConnectionStatus.state(key))
    }
}
