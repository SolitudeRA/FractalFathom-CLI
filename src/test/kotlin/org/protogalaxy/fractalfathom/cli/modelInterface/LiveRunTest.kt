package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.logging.LoggerFactory
import java.io.IOException
import java.net.Socket

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class LiveRunTest {

    private var serverProcess: Process? = null
    private var logger = LoggerFactory.getLogger(LiveRunTest::class.java)

    @BeforeAll
    fun setup() {
        serverProcess = ProcessBuilder(
            "python3",
            "src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"
        ).apply {
            redirectOutput(ProcessBuilder.Redirect.INHERIT)
            redirectError(ProcessBuilder.Redirect.INHERIT)
        }.start()

        val maxWaitTime = 10_000L // 30 seconds
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
        serverProcess?.apply {
            destroy()
            waitFor()
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