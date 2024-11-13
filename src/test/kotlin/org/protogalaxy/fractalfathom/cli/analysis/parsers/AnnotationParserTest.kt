package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest

class AnnotationParserTest : BaseTest() {

    @Test
    fun testParseClassAnnotations() {
        val annotations = ctClass.annotations

        val annotationParser = AnnotationParser()
        val annotationEntities = annotations.map { annotationParser.parseAnnotation(it) }

        assertEquals(2, annotationEntities.size, "Expected 2 annotations")

        val featureAnnotation = annotationEntities.find { it.name == "org.protogalaxy.fractalfathom.cli.resources.annotations.Feature" }
        val mappingAnnotation = annotationEntities.find { it.name == "org.protogalaxy.fractalfathom.cli.resources.annotations.Mapping" }

        assertNotNull(featureAnnotation, "Feature annotation should not be null")
        assertNotNull(mappingAnnotation, "Mapping annotation should not be null")

        // 验证 Feature 注解的属性
        assertAll("Feature Annotation Properties",
            { assertEquals("UserService", featureAnnotation?.attributes?.get("name"), "Feature name should match") },
            { assertEquals("Handles user-related operations", featureAnnotation?.attributes?.get("description"), "Feature description should match") },
            { assertEquals("FUNCTIONAL", featureAnnotation?.attributes?.get("type"), "Feature type should match") }
        )

        // 验证 Mapping 注解的属性
        assertAll("Mapping Annotation Properties",
            { assertEquals("User Management", mappingAnnotation?.attributes?.get("toConcept"), "Mapping toConcept should match") },
            { assertEquals("MODULE", mappingAnnotation?.attributes?.get("type"), "Mapping type should match") }
        )
    }
}