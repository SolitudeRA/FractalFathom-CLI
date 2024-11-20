package org.protogalaxy.fractalfathom.cli.analysis.annotation

/**
 * Represents a mapping entity to a higher-level concept or design pattern.
 *
 * @property toConcept The target high-level concept or design pattern being mapped to.
 * @property type The type of mapping, defaulting to concept mapping.
 * @property subMappings Optional list of sub-mappings, allowing nested mappings.
 */
data class MappingEntity(
    val toConcept: String,
    val type: MappingType = MappingType.CONCEPT,
    val subMappings: List<MappingEntity>? = null
)