package org.protogalaxy.fractalfathom.cli.analysis.annotation

data class AnnotationLifecycle (
    val isActive: Boolean,                          // 当前注解是否生效
    val startCondition: String? = null,             // 启动条件
    val endCondition: String? = null                // 结束条件
)