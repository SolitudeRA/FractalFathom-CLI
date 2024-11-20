package org.protogalaxy.fractalfathom.cli.analysis.annotation

/**
 * Enum representing the application phase of an annotation.
 */
enum class AnnotationPhase {
    COMPILE_TIME,   // Applied at compile-time
    RUNTIME,        // Applied at runtime
    STATIC_ANALYSIS // Applied during static analysis
}