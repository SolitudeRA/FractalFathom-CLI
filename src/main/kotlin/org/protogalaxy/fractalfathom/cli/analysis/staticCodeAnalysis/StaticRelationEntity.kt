package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

data class StaticRelationEntity(
    val relationType: String,                      // 关系类型（如 "extends", "implements"）
    val targetClass: String,                       // 关系目标类
)
