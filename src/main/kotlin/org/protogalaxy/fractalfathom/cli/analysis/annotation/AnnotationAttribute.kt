package org.protogalaxy.fractalfathom.cli.analysis.annotation

/**
 * Represents a single attribute within an annotation.
 *
 * @property key The name of the attribute.
 * @property value The value of the attribute, supporting multiple types.
 */
data class AnnotationAttribute(
    val key: String,
    val value: Any
)
