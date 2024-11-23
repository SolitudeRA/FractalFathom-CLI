package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.logging.LoggerFactory
import java.io.File
import java.io.IOException
import java.net.Socket
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class LiveRunTest {

    private var serverProcess: Process? = null
    private var logger = LoggerFactory.getLogger(LiveRunTest::class.java)

    @BeforeAll
    fun setup() {
        serverProcess = ProcessBuilder(
            "python3",
            "./src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"
        ).directory(File(System.getProperty("user.dir"))).apply {
            redirectOutput(ProcessBuilder.Redirect.INHERIT)
            redirectError(ProcessBuilder.Redirect.INHERIT)
        }.start()

        val maxWaitTime = 30_000L // 30 seconds
        val startTime = System.currentTimeMillis()
        while (!isServerRunning()) {
            if (System.currentTimeMillis() - startTime > maxWaitTime) {
                throw IllegalStateException("Server did not start within $maxWaitTime ms")
            }
            Thread.sleep(500)
        }
    }

    @AfterAll
    fun tearDown() {
        serverProcess?.let {
            if (it.isAlive) {
                it.destroy()
                it.waitFor(30, TimeUnit.SECONDS)
            }
        }
    }

    private fun isServerRunning(): Boolean {
        return try {
            Socket("127.0.0.1", 5000).use { true }
        } catch (_: IOException) {
            false
        }
    }
}