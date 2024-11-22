package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType

class MethodParserTest : BaseTest() {

    @Test
    fun testParseMethods() {
        val methods = ctClass.methods

        val methodParser = MethodParser()
        val methodEntities = methods.map { methodParser.parseMethod(it) }

        assertEquals(2, methodEntities.size, "Expected 2 methods in the class")

        val createUserMethod = methodEntities.find { it.name == "createUser" }
        val deleteUserMethod = methodEntities.find { it.name == "deleteUser" }

        assertNotNull(createUserMethod, "createUser method should not be null")
        assertNotNull(deleteUserMethod, "deleteUser method should not be null")

        assertEquals(1, createUserMethod?.mappings?.size, "Expected 1 mappings on createUser method")
        val mappingAnnotation = createUserMethod?.mappings?.get(0)

        assertNotNull(mappingAnnotation, "Mapping annotation should not be null on createUser method")

        assertAll("Mapping Annotation Properties",
            { assertEquals("User Creation", mappingAnnotation?.toConcept, "Mapping toConcept attribute should match") },
            { assertEquals(MappingType.COMPONENT, mappingAnnotation?.type, "Mapping type attribute should match") }
        )
    }
}
