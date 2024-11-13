package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.LowLevelAST
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticCallEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticParameterEntity


data class IRMethodEntity(
    val name: String,                               // 方法名
    val returnType: String,                         // 返回类型
    val parameters: List<StaticParameterEntity>,    // 参数列表
    val modifiers: String,                          // 方法修饰符
    val annotations: List<AnnotationEntity>,        // 方法上的注解
    val features: List<FeatureEntity>,              // 方法的功能特性
    val mappings: List<MappingEntity>,              // 方法的业务逻辑映射
    val calledMethods: List<StaticCallEntity>,      // 被调用的方法列表
    val lowLevelAST: LowLevelAST?,                  // 方法体的低层AST
    val sourceCodeLocation: SourceCodeLocation,     // 方法的源码位置
    val embedding: Embedding? = null                // 方法的嵌入向量
)
