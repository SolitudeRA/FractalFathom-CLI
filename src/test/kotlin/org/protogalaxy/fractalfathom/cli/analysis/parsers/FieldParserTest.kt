package org.protogalaxy.fractalfathom.cli.analysis.parsers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import org.protogalaxy.fractalfathom.cli.analysis.BaseTest
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType

class FieldParserTest : BaseTest() {

    private val logger = LoggerFactory.getLogger(FieldParserTest::class.java)
    private val mapper = jacksonObjectMapper()

    @Test
    fun testParseFields() {
        val fields = ctClass.fields

        val fieldParser = FieldParser()
        val fieldEntities = fields.map { fieldParser.parseField(it) }

        assertEquals(2, fieldEntities.size, "Expected 2 field in the class")

        val userRepositoryField = fieldEntities.find { it.name == "userRepository" }

        logger.info {
            "User Repository Field: ${
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userRepositoryField)
            }"
        }

        assertNotNull(userRepositoryField, "userRepository field should not be null")
        assertEquals("org.protogalaxy.fractalfathom.cli.resources.UserRepository", userRepositoryField?.type, "Field type should match")
        assertEquals(1, userRepositoryField?.mappings?.size, "Expected 1 mappings on the field")

        val mappingAnnotation = userRepositoryField?.mappings?.first()

        assertNotNull(mappingAnnotation, "Mappings annotation should not be null")
        assertAll(
            "Mappings Annotation Properties",
            { assertEquals("User Repository", mappingAnnotation?.toConcept) },
            { assertEquals(MappingType.COMPONENT, mappingAnnotation?.type, "Annotation name should match") },
        )
    }
}
