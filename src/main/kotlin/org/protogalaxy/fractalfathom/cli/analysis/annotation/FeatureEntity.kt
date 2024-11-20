package org.protogalaxy.fractalfathom.cli.analysis.annotation

/**
 * Represents a feature entity with its properties and associated annotations.
 *
 * @property name The name of the feature.
 * @property description Optional description of the feature.
 * @property type The type of feature, indicating whether it is functional or non-functional.
 * @property subFeatures Optional list of sub-features, allowing nested features.
 */
data class FeatureEntity(
    val name: String,
    val description: String? = null,
    val type: FeatureType,
    val subFeatures: List<FeatureEntity>? = null
)