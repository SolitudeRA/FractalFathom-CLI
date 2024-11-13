package org.protogalaxy.fractalfathom.cli.analysis.ir

data class ComplexityMetrics(
    val cyclomaticComplexity: Int,                 // 圈复杂度
    val nestingDepth: Int,                         // 嵌套深度
    val branchCount: Int                           // 条件分支数量
)
