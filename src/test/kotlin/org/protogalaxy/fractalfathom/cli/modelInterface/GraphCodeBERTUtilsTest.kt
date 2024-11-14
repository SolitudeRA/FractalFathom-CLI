package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationPhase
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationTargetType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType
import org.protogalaxy.fractalfathom.cli.analysis.ir.ComplexityMetrics
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphCodeBERTUtilsTest {

    private lateinit var graphCodeBERTUtils: GraphCodeBERTUtils
    private var serverProcess: Process? = null

    @BeforeAll
    fun setup() {
        // 启动Python服务器
        serverProcess = ProcessBuilder(
            "python",
            "src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"
        ).redirectOutput(ProcessBuilder.Redirect.INHERIT).redirectError(ProcessBuilder.Redirect.INHERIT).start()

        // 等待服务器启动
        Thread.sleep(5000) // 等待 5 秒，确保服务器已启动
        graphCodeBERTUtils = GraphCodeBERTUtils()
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
    fun enhanceIRDataWithEmbeddingsTest() {
        // 准备测试数据
        val irClass = IRClassEntity(
            name = "TestClass",
            type = "Class",
            packageName = "org.protogalaxy",
            filePath = "src/TestClass.java",
            modifiers = "public",
            superClass = null,
            interfaces = listOf("Serializable"),
            annotations = listOf(
                AnnotationEntity(
                    name = "TestAnnotation",
                    attributes = emptyMap(),
                    targetElement = "",
                    targetType = AnnotationTargetType.CLASS,
                    condition = null,
                    dependencies = emptyList(),
                    phase = AnnotationPhase.RUNTIME
                )
            ),
            features = listOf(
                FeatureEntity(
                    name = "TestFeature",
                    description = null,
                    type = FeatureType.FUNCTIONAL,
                    subFeatures = emptyList(),
                    annotations = emptyList()
                )
            ),
            mappings = listOf(
                MappingEntity(
                    toConcept = "BusinessLogic",
                    type = MappingType.CONCEPT,
                    subMappings = emptyList(),
                    annotations = emptyList()
                )
            ),
            fields = listOf(
                IRFieldEntity(
                    name = "field1",
                    type = "int",
                    modifiers = "private",
                    annotations = emptyList(),
                    features = emptyList(),
                    mappings = emptyList(),
                    sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 1, 1, 10)
                )
            ),
            methods = listOf(
                IRMethodEntity(
                    name = "method1",
                    returnType = "void",
                    parameters = emptyList(),
                    modifiers = "public",
                    annotations = emptyList(),
                    features = emptyList(),
                    mappings = emptyList(),
                    calledMethods = emptyList(),
                    lowLevelAST = null,
                    sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 1, 1, 10)
                )
            ),
            relations = emptyList(),
            sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 10, 1, 1),
            complexityMetrics = ComplexityMetrics(cyclomaticComplexity = 1, nestingDepth = 1, branchCount = 0),
            embedding = null
        )

        // 调用 GraphCodeBERTUtils 增强 IR 数据
        val enhancedIRClasses = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(listOf(irClass))

        // 验证返回的数据是否包含嵌入向量
        val enhancedClass = enhancedIRClasses.first()
        assertNotNull(enhancedClass.embedding)
        assertEquals(enhancedClass.embedding?.values?.size, 32)
        assertTrue(enhancedClass.embedding!!.values.isNotEmpty())
    }
}