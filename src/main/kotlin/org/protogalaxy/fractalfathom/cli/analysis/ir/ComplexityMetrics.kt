package org.protogalaxy.fractalfathom.cli.analysis.ir

/**
 * Represents complexity metrics for a code entity.
 *
 * @property cyclomaticComplexity The cyclomatic complexity of the code.
 * @property nestingDepth The maximum nesting depth in the code.
 * @property branchCount The number of conditional branches in the code.
 */

data class ComplexityMetrics(
    val cyclomaticComplexity: Int,                 // 圈复杂度
    val nestingDepth: Int,                         // 嵌套深度
    val branchCount: Int                           // 条件分支数量
)
