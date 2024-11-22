package org.protogalaxy.fractalfathom.cli.modelInterface

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.protogalaxy.fractalfathom.cli.analysis.CodeAnalyzer
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils


class LLMUtilsPromptTest : LiveRunTest() {

    private lateinit var llmUtils: LLMUtils

    private val logger = LoggerFactory.getLogger(LLMUtilsPromptTest::class.java)

    @Test
    fun testGeneratingPrompt() = runBlocking {
        llmUtils = LLMUtils()

        val codeAnalyzer = CodeAnalyzer("src/test/kotlin/org/protogalaxy/fractalfathom/cli/resources")
        val irData = codeAnalyzer.analyzeProject()

        val graphCodeBERTUtils = GraphCodeBERTUtils()
        val enhancedIrData = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(irData)

        val prompt = llmUtils.constructPrompt(enhancedIrData)

        logger.info { "Generated prompt:\n $prompt" }
    }
}