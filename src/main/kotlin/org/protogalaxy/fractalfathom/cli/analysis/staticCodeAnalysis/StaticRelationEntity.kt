package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

/**
 * Represents a static relationship between classes, such as inheritance or implementation.
 *
 * @property relationType The type of the relationship (e.g., "extends", "implements").
 * @property targetClass The name of the target class involved in the relationship.
 */
data class StaticRelationEntity(
    val relationType: String,                      // 关系类型（如 "extends", "implements"）
    val targetClass: String,                       // 关系目标类
)
