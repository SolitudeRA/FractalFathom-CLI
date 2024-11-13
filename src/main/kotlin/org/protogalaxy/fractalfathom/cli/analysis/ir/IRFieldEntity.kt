package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity


data class IRFieldEntity(
    val name: String,                               // 字段名称
    val type: String,                               // 字段类型
    val modifiers: String,                          // 字段修饰符
    val annotations: List<AnnotationEntity>,        // 字段上的注解
    val features: List<FeatureEntity>,              // 字段的功能特性
    val mappings: List<MappingEntity>,              // 字段的业务逻辑映射
    val sourceCodeLocation: SourceCodeLocation,     // 字段在源码中的位置
    val embedding: Embedding? = null                // 字段的嵌入向量
)