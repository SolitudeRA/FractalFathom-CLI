package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import spoon.reflect.declaration.CtField

/**
 * A parser for converting Spoon's `CtField` representation of a Java field
 * into a custom `IRFieldEntity` representation.
 */
class FieldParser {

    private val annotationProcessor = AnnotationProcessor()

    /**
     * Parses a Spoon `CtField` object into a custom `IRFieldEntity`.
     *
     * @param ctField The Spoon `CtField` object representing a Java field.
     * @return An `IRFieldEntity` containing the field's metadata, annotations, and source code location.
     */
    fun parseField(ctField: CtField<*>): IRFieldEntity {
        val name = ctField.simpleName
        val type = ctField.type.qualifiedName
        val modifiers = ctField.modifiers.joinToString(" ")

        val annotationParser = AnnotationParser()
        val parsedAnnotations = ctField.annotations.map { annotationParser.parseAnnotation(it) }
        val (features, mappings, annotations) = annotationProcessor.processAnnotations(parsedAnnotations)

        val sourceCodeLocation = SourceCodeLocation(
            filePath = ctField.position?.file?.path ?: "",
            startLine = ctField.position?.line ?: 0,
            endLine = ctField.position?.endLine ?: 0,
            startColumn = ctField.position?.column ?: 0,
            endColumn = ctField.position?.endColumn ?: 0
        )

        return IRFieldEntity(
            name = name,
            type = type,
            modifiers = modifiers,
            annotations = annotations,
            features = features,
            mappings = mappings,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}