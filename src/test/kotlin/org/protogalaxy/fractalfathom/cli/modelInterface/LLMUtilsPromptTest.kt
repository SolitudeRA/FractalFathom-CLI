package org.protogalaxy.fractalfathom.cli.modelInterface

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.platform.commons.logging.LoggerFactory
import org.protogalaxy.fractalfathom.cli.analysis.CodeAnalyzer
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LLMUtilsPromptTest {

    private lateinit var llmUtils: LLMUtils
    private var serverProcess: Process? = null

    private val logger = LoggerFactory.getLogger(LLMUtilsPromptTest::class.java)
    private val mapper = jacksonObjectMapper()

    @BeforeAll
    fun setup() {
        // 启动Python服务器
        serverProcess = ProcessBuilder(
            "python3",
            "src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"
        ).apply {
            redirectOutput(ProcessBuilder.Redirect.INHERIT)
            redirectError(ProcessBuilder.Redirect.INHERIT)
        }.start()

        // 等待服务器启动
        Thread.sleep(5000) // 等待 5 秒，确保服务器已启动
        llmUtils = LLMUtils()
    }

    @AfterAll
    fun tearDown() {
        // 关闭Python服务器
        serverProcess?.apply {
            destroy()
            waitFor()
        }
    }

    @Test
    fun testGeneratingPrompt() = runBlocking() {

        val codeAnalyzer = CodeAnalyzer("src/test/kotlin/org/protogalaxy/fractalfathom/cli/resources")
        val irData = codeAnalyzer.analyzeProject()

        val graphCodeBERTUtils = GraphCodeBERTUtils()
        val enhancedIrData = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(irData)

        logger.info { "Enhanced IR data:\n ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(enhancedIrData)}" }

        val llmUtils = LLMUtils()
        val prompt = llmUtils.constructPrompt(enhancedIrData)

        logger.info { "Generated prompt:\n $prompt" }
    }
}