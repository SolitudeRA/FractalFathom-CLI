package org.protogalaxy.fractalfathom.cli.modelInterface

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationPhase
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationTargetType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType
import org.protogalaxy.fractalfathom.cli.analysis.ir.ComplexityMetrics
import org.protogalaxy.fractalfathom.cli.analysis.ir.Embedding
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils

class LLMUtilsDryTest {
    private val logger = LoggerFactory.getLogger(GraphCodeBERTUtilsTest::class.java)

    @Test
    fun testConstructPrompt() {

        // 实例化LLMUtils
        val llmUtils = LLMUtils()

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
                    sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 1, 1, 10),
                    embedding = Embedding(values = doubleArrayOf(1.0, 2.0, 3.0).toList())
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
                    sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 1, 1, 10),
                    embedding = Embedding(values = doubleArrayOf(1.0, 2.0, 3.0).toList())
                )
            ),
            relations = emptyList(),
            sourceCodeLocation = SourceCodeLocation("src/TestClass.java", 1, 10, 1, 1),
            complexityMetrics = ComplexityMetrics(cyclomaticComplexity = 1, nestingDepth = 1, branchCount = 0),
            embedding = Embedding(values = doubleArrayOf(1.0, 2.0, 3.0).toList())
        )

        // 调用测试方法
        val prompt = llmUtils.constructPrompt(listOf(irClass))

        logger.info { "Prompt: $prompt" }

        assertTrue(prompt.contains("Class: TestClass (Type: Class)"), "Class information is incorrect")
        assertTrue(prompt.contains("Features:\n- TestFeature"), "Features section is missing or incorrect")
        assertTrue(prompt.contains("Mappings:\n- BusinessLogic"), "Mappings section is missing or incorrect")
        assertTrue(prompt.contains("Fields:\n- private int field1"), "Fields section is missing or incorrect")
        assertTrue(prompt.contains("Methods:\n- public void method1()"), "Methods section is missing or incorrect")
        assertTrue(prompt.contains("Embeddings:\n- 1.0, 2.0, 3.0"), "Embedding section is missing or incorrect")
    }
}