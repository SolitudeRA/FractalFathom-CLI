package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationPhase
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationTargetType
import spoon.reflect.declaration.*

/**
 * A parser for processing annotations in Java source code using Spoon.
 *
 * This class is responsible for converting Spoon's `CtAnnotation` representation
 * into a custom `AnnotationEntity` object that includes metadata, attributes,
 * and source code location information.
 */
class AnnotationParser {

    /**
     * Parses a Spoon `CtAnnotation` object into a custom `AnnotationEntity`.
     *
     * @param ctAnnotation The Spoon `CtAnnotation` object to be parsed.
     * @return An `AnnotationEntity` object containing the parsed data.
     */
    fun parseAnnotation(ctAnnotation: CtAnnotation<*>): AnnotationEntity {
        // Extract the fully qualified name of the annotation
        val name = ctAnnotation.annotationType.qualifiedName

        // Extract annotation attributes and process special cases
        val attributes = ctAnnotation.values.mapValues {
            if (it.key == "type") {
                it.value.toString().replaceBeforeLast(".", "").removeSurrounding(".", "")
            } else {
                it.value.toString().removeSurrounding("\"", "\"")
            }
        }

        val targetElement = ctAnnotation.parent?.toString() ?: "Unknown"
        // Map Spoon's annotated element type to custom `AnnotationTargetType`
        val targetType = when (ctAnnotation.annotatedElementType) {
            CtAnnotatedElementType.TYPE -> AnnotationTargetType.CLASS
            CtAnnotatedElementType.FIELD -> AnnotationTargetType.FIELD
            CtAnnotatedElementType.METHOD -> AnnotationTargetType.METHOD
            else -> AnnotationTargetType.CLASS
        }

        // Extract source code location from the annotation's position in the source file
        val sourceCodeLocation = ctAnnotation.position?.let {
            SourceCodeLocation(
                filePath = it.file?.path ?: "",
                startLine = it.line,
                endLine = it.endLine,
                startColumn = it.column,
                endColumn = it.endColumn
            )
        } ?: SourceCodeLocation("", -1, -1, -1, -1)

        return AnnotationEntity(
            name = name,
            attributes = attributes,
            targetElement = targetElement,
            targetType = targetType,
            condition = null,
            dependencies = null,
            phase = AnnotationPhase.RUNTIME,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}