package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity

/**
 * Represents a field entity in the intermediate representation (IR).
 *
 * @property name The name of the field.
 * @property type The type of the field.
 * @property modifiers The modifiers applied to the field (e.g., private, static).
 * @property annotations A list of annotations applied to the field.
 * @property features A list of feature entities associated with the field.
 * @property mappings A list of business logic mappings related to the field.
 * @property sourceCodeLocation The location of the field in the source code.
 * @property embedding The embedding vector for the field, optional.
 */
data class IRFieldEntity(
    val name: String,
    val type: String,
    val modifiers: String,
    val annotations: List<AnnotationEntity>,
    val features: List<FeatureEntity>,
    val mappings: List<MappingEntity>,
    val sourceCodeLocation: SourceCodeLocation,
    val embedding: Embedding? = null
)