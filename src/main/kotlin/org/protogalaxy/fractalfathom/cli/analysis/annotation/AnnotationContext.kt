package org.protogalaxy.fractalfathom.cli.analysis.annotation

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

data class AnnotationContext (
    val targetElement: String,                      // 注解作用的代码元素名称
    val targetType: AnnotationTargetType,           // 注解作用的元素类型（如类、方法、字段）
    val scope: AnnotationScopeType,                 // 注解的作用范围（如全局、局部）
    val applicationPhase: AnnotationPhase,          // 注解的应用阶段（如编译期、运行期等）
    val sourceCodeLocation: SourceCodeLocation? = null  // 注解在源码中的位置
)