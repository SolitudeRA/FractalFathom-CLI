package org.protogalaxy.fractalfathom.cli.analysis.annotation

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

/**
 * Represents an annotation entity with its properties, target, and lifecycle details.
 *
 * @property name The name of the annotation.
 * @property attributes A map containing the key-value pairs of annotation attributes.
 * @property targetElement The name of the code element the annotation applies to (e.g., class name, method name, field name).
 * @property targetType The type of the code element (e.g., CLASS, METHOD, FIELD).
 * @property condition Optional condition that determines when the annotation is active.
 * @property dependencies Optional list of dependent annotation names.
 * @property phase The phase during which the annotation is effective (e.g., compile-time, runtime).
 * @property sourceCodeLocation The location of the annotation in the source code, optional.
 */
data class AnnotationEntity(
    val name: String,
    val attributes: Map<String, Any>,
    val targetElement: String,
    val targetType: AnnotationTargetType,
    val condition: String? = null,
    val dependencies: List<String>? = null,
    val phase: AnnotationPhase,
    val sourceCodeLocation: SourceCodeLocation? = null
)
