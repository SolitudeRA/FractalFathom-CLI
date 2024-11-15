package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest

class ClassParserTest : BaseTest() {

    @Test
    fun testParseClass() {
        val classParser = ClassParser()
        val classEntity = classParser.parseClass(ctClass, "UserService.java")

        assertEquals("UserService", classEntity.name, "Class name should be 'UserService'")
        assertEquals("Class", classEntity.type, "Class type should be 'Class'")
        assertEquals("org.protogalaxy.fractalfathom.cli.resources", classEntity.packageName, "Package name should match")
        assertEquals("public", classEntity.modifiers, "Modifiers should be 'public'")
        assertNull(classEntity.superClass, "Superclass should be null when there is no explicit parent class")
        assertTrue(classEntity.interfaces.isEmpty(), "Interfaces should be empty")

        // 验证类的注解
        assertEquals(2, classEntity.annotations.size, "Expected 2 annotations on the class")
        val featureAnnotation = classEntity.annotations.find { it.name == "org.protogalaxy.fractalfathom.FractalFathomFeature" }
        val mappingAnnotation = classEntity.annotations.find { it.name == "org.protogalaxy.fractalfathom.FractalFathomMapping" }

        assertNotNull(featureAnnotation, "Feature annotation should not be null")
        assertNotNull(mappingAnnotation, "Mapping annotation should not be null")

        // 验证字段和方法数量
        assertEquals(1, classEntity.fields.size, "Expected 1 field in the class")
        assertEquals(2, classEntity.methods.size, "Expected 2 methods in the class")
    }
}
