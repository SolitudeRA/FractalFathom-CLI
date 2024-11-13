package org.protogalaxy.fractalfathom.cli.analysis.annotation

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

data class AnnotationEntity(
    val name: String,                               // 注解名称
    val attributes: Map<String, Any>,               // 注解的属性键值对
    val targetElement: String,                      // 注解作用的代码元素（类名、方法名、字段名）
    val targetType: AnnotationTargetType,           // 注解作用的元素类型（CLASS, METHOD, FIELD）
    val condition: String? = null,                  // 激活条件
    val dependencies: List<String>? = null,         // 依赖的其他注解名称
    val phase: AnnotationPhase,                     // 注解生效阶段（如编译期、运行期等）
    val sourceCodeLocation: SourceCodeLocation? = null  // 注解的源码位置
)
