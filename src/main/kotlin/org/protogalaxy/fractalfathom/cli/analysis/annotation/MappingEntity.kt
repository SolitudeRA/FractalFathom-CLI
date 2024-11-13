package org.protogalaxy.fractalfathom.cli.analysis.annotation

data class MappingEntity(
    val toConcept: String,                          // 映射的目标高级概念或设计模式
    val type: MappingType = MappingType.CONCEPT,    // 映射类型，默认为概念映射
    val subMappings: List<MappingEntity>? = null,   // 子映射列表，支持嵌套映射
    val annotations: List<AnnotationEntity>? = null // 关联的注解信息
)