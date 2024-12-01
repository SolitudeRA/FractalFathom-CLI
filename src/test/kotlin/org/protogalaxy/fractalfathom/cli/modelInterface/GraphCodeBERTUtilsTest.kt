package org.protogalaxy.fractalfathom.cli.modelInterface

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
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
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils

class GraphCodeBERTUtilsTest : LiveRunTest() {

    private lateinit var graphCodeBERTUtils: GraphCodeBERTUtils

    private val mapper = jacksonObjectMapper().apply{
        registerModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
    private val logger = LoggerFactory.getLogger(GraphCodeBERTUtilsTest::class.java)

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
                FeatureEntity.create(
                    name = "TestFeature",
                    description = null,
                    type = FeatureType.FUNCTIONAL
                )
            ),
            mappings = listOf(
                MappingEntity.create(
                    toConcept = "BusinessLogic",
                    type = MappingType.CONCEPT
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

        graphCodeBERTUtils = GraphCodeBERTUtils()

        val enhancedIRClasses = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(listOf(irClass))

        logger.info { "Enhanced IR Classes: ${mapper.writerWithDefaultPrettyPrinter().writeValueAsString(enhancedIRClasses)}" }

        val enhancedClass = enhancedIRClasses.first()
        assertNotNull(enhancedClass.embedding)
        assertEquals(enhancedClass.embedding?.values?.size, 32)
        assertTrue(enhancedClass.embedding!!.values.isNotEmpty())
    }
}