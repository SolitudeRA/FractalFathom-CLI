package org.protogalaxy.fractalfathom.cli.analysis.annotation

data class AnnotationAttribute(
    val key: String,                                // 属性名称
    val value: Any                                  // 属性值，支持多种类型
)
