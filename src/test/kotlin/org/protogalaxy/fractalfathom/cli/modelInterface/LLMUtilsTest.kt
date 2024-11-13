package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.nio.file.Paths
import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.ComplexityMetrics
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LLMUtilsTest {

    private lateinit var llmUtils: LLMUtils
    private var serverProcess: Process? = null

    @BeforeAll
    fun setup() {
        val envFilePath = Paths.get(".env")
        val envMap = Files.readAllLines(envFilePath).associate { line ->
            val (key, value) = line.split("=")
            key.trim() to value.trim()
        }

        // 启动Python服务器
        serverProcess = ProcessBuilder(
            "python3",
            "src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"
        ).apply {
            environment().putAll(envMap)
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

        // 调用 generatePlantUML 方法
        val plantUMLCode = llmUtils.generatePlantUML(listOf(irClassExample))

        // 检查返回的PlantUML代码
        assertNotNull(plantUMLCode, "生成的PlantUML代码不应为空")
        println("Generated PlantUML code:\n$plantUMLCode")
    }
}