package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticRelationEntity


data class IRClassEntity(
    val name: String,                               // 类名
    val type: String,                               // 类的类型（如 "Class", "Interface" 等）
    val packageName: String,                        // 包名
    val filePath: String,                           // 文件路径
    val modifiers: String,                          // 类的修饰符（如 public, abstract 等）
    val superClass: String?,                        // 父类名称（可选）
    val interfaces: List<String>,                   // 实现的接口列表
    val annotations: List<AnnotationEntity>,        // 类上的注解信息
    val features: List<FeatureEntity>,              // 功能特性列表
    val mappings: List<MappingEntity>,              // 业务逻辑映射列表
    val fields: List<IRFieldEntity>,                // 字段列表
    val methods: List<IRMethodEntity>,              // 方法列表
    val relations: List<StaticRelationEntity>,      // 类的继承或实现关系
    val sourceCodeLocation: SourceCodeLocation,     // 类在源码中的位置
    val complexityMetrics: ComplexityMetrics,       // 类的复杂度度量
    val embedding: Embedding? = null                // 类的嵌入向量
)
