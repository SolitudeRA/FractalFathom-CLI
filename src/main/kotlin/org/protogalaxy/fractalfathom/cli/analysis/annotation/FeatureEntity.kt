package org.protogalaxy.fractalfathom.cli.analysis.annotation

data class FeatureEntity(
    val name: String,                               // 功能特性名称
    val description: String? = null,                // 功能特性描述（可选）
    val type: FeatureType,                          // 功能特性类型，表示功能或非功能特性
    val subFeatures: List<FeatureEntity>? = null,   // 子特性列表，支持嵌套特性
    val annotations: List<AnnotationEntity>         // 关联的注解信息
)