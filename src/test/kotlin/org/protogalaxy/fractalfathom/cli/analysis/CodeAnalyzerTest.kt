package org.protogalaxy.fractalfathom.cli.analysis

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory

class CodeAnalyzerTest {

    private val mapper = jacksonObjectMapper().apply{
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
    private val logger = LoggerFactory.getLogger(CodeAnalyzerTest::class.java)

    @Test
    fun codeAnalyzerMonitoringTest() = runBlocking {
        val codeAnalyzer = CodeAnalyzer("src/test/kotlin/org/protogalaxy/fractalfathom/cli/resources")
        val irData = codeAnalyzer.analyzeProject()

        logger.info { "IR Data:\n ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(irData)}" }
    }
}