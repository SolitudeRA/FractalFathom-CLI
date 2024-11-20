package org.protogalaxy.fractalfathom.cli.analysis.annotation

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

/**
 * Contextual information about an annotation, including its scope and location.
 *
 * @property targetElement The name of the code element to which the annotation is applied.
 * @property targetType The type of the target element (e.g., class, method, or field).
 * @property scope The scope of the annotation (e.g., global or local).
 * @property applicationPhase The phase in which the annotation is applied (e.g., compile-time, runtime).
 * @property sourceCodeLocation The location of the annotation in the source code, optional.
 */
data class AnnotationContext(
    val targetElement: String,
    val targetType: AnnotationTargetType,
    val scope: AnnotationScopeType,
    val applicationPhase: AnnotationPhase,
    val sourceCodeLocation: SourceCodeLocation? = null
)