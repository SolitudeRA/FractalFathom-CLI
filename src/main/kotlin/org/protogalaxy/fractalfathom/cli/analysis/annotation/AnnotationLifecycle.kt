package org.protogalaxy.fractalfathom.cli.analysis.annotation

/**
 * Represents the lifecycle state of an annotation.
 *
 * @property isActive Indicates whether the annotation is currently active.
 * @property startCondition Optional condition for activating the annotation.
 * @property endCondition Optional condition for deactivating the annotation.
 */
data class AnnotationLifecycle(
    val isActive: Boolean,
    val startCondition: String? = null,
    val endCondition: String? = null
)