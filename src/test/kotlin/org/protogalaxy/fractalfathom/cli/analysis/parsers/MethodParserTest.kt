package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest

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

        // 验证 createUser 方法的注解
        assertEquals(2, createUserMethod?.annotations?.size, "Expected 2 annotations on createUser method")
        val featureAnnotation = createUserMethod?.annotations?.find { it.name == "org.protogalaxy.fractalfathom.FractalFathomFeature" }
        val mappingAnnotation = createUserMethod?.annotations?.find { it.name == "org.protogalaxy.fractalfathom.FractalFathomMapping" }

        assertNotNull(featureAnnotation, "Feature annotation should not be null on createUser method")
        assertNotNull(mappingAnnotation, "Mapping annotation should not be null on createUser method")

        assertAll("Feature Annotation Properties",
            { assertEquals("createUser", featureAnnotation?.attributes?.get("name"), "Feature name attribute should match") },
            { assertEquals("Creates a new user", featureAnnotation?.attributes?.get("description"), "Feature description attribute should match") },
            { assertEquals("FUNCTIONAL", featureAnnotation?.attributes?.get("type"), "Feature type attribute should match") }
        )

        assertAll("Mapping Annotation Properties",
            { assertEquals("User Creation", mappingAnnotation?.attributes?.get("toConcept"), "Mapping toConcept attribute should match") },
            { assertEquals("COMPONENT", mappingAnnotation?.attributes?.get("type"), "Mapping type attribute should match") }
        )
    }
}
