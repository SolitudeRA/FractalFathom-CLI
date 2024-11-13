package org.protogalaxy.fractalfathom.cli.analysis.ir

data class IRDependencyEdge(
    val fromElement: String,                       // 起始元素
    val toElement: String,                         // 目标元素
    val relationType: String                       // 关系类型（如 "calls", "inherits"）
)
