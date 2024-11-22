package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType

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

        assertEquals(0, classEntity.annotations.size, "Annotations should not include feature or mapping annotations")

        assertEquals(1, classEntity.features.size, "Expected 1 feature in the class")
        assertEquals(1, classEntity.mappings.size, "Expected 1 mapping in the class")

        val featureEntity = classEntity.features.find { it.name == "UserService" }
        val mappingEntity = classEntity.mappings.find { it.toConcept == "User Management" }

        assertNotNull(featureEntity, "Feature entity should not be null")
        assertEquals("UserService", featureEntity?.name, "Feature name should match")
        assertEquals(FeatureType.FUNCTIONAL, featureEntity?.type, "Feature type should be FUNCTIONAL")

        assertNotNull(mappingEntity, "Mapping entity should not be null")
        assertEquals("User Management", mappingEntity?.toConcept, "Mapping target concept should match")
        assertEquals(MappingType.MODULE, mappingEntity?.type, "Mapping type should be CONCEPT")

        assertEquals(2, classEntity.fields.size, "Expected 2 fields in the class")
        assertEquals(3, classEntity.methods.size, "Expected 3 methods in the class")
    }
}
