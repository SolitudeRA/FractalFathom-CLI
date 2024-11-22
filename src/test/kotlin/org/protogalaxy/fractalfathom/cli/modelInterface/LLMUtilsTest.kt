package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.platform.commons.logging.LoggerFactory
import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.ComplexityMetrics
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils

class LLMUtilsTest : LiveRunTest() {

    private lateinit var llmUtils: LLMUtils
    private val logger = LoggerFactory.getLogger(LLMUtilsTest::class.java)

    @Test
    fun testGeneratePlantUML() {
        // 创建IRClassEntity示例数据
        val irClassExample = IRClassEntity(
            name = "ExampleClass",
            type = "Class",
            packageName = "org.example",
            filePath = "src/org/example/ExampleClass.java",
            modifiers = "public",
            superClass = "BaseClass",
            interfaces = listOf("ExampleInterface"),
            annotations = emptyList(),
            features = emptyList(),
            mappings = emptyList(),
            fields = emptyList(),
            methods = emptyList(),
            relations = emptyList(),
            sourceCodeLocation = SourceCodeLocation("src/org/example/ExampleClass.java", 1, 100, 0, 10),
            complexityMetrics = ComplexityMetrics(5, 2, 3),
            embedding = null
        )

        llmUtils = LLMUtils()

        // 调用 generatePlantUML 方法
        val plantUMLCode = llmUtils.generatePlantUML(listOf(irClassExample))

        // 检查返回的PlantUML代码
        assertNotNull(plantUMLCode, "生成的PlantUML代码不应为空")
        logger.info { "Generated PlantUML code:\n$plantUMLCode" }
    }
}