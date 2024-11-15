package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest

class FieldParserTest : BaseTest() {

    @Test
    fun testParseFields() {
        val fields = ctClass.fields

        val fieldParser = FieldParser()
        val fieldEntities = fields.map { fieldParser.parseField(it) }

        assertEquals(1, fieldEntities.size, "Expected 1 field in the class")

        val userRepositoryField = fieldEntities.find { it.name == "userRepository" }

        assertNotNull(userRepositoryField, "userRepository field should not be null")

        assertEquals("org.protogalaxy.fractalfathom.cli.resources.UserRepository", userRepositoryField?.type, "Field type should match")

        // 验证字段上的注解
        assertEquals(1, userRepositoryField?.annotations?.size, "Expected 1 annotation on the field")
        val featureAnnotation = userRepositoryField?.annotations?.first()
        assertNotNull(featureAnnotation, "Feature annotation should not be null")

        assertAll("Feature Annotation Properties",
            { assertEquals("org.protogalaxy.fractalfathom.FractalFathomFeature", featureAnnotation?.name, "Annotation name should match") },
            { assertEquals("userRepository", featureAnnotation?.attributes?.get("name"), "Annotation attribute 'name' should match") },
            { assertEquals("Repository for accessing user data", featureAnnotation?.attributes?.get("description"), "Annotation attribute 'description' should match") },
            { assertEquals("NON_FUNCTIONAL", featureAnnotation?.attributes?.get("type"), "Annotation attribute 'type' should match") }
        )
    }
}
