package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

data class StaticStatementEntity(
    val type: String,                              // 语句类型（如赋值、条件语句等）
    val expression: String?,                       // 表达式内容
    val subStatements: List<StaticStatementEntity>?, // 子语句列表
    val sourceCodeLocation: SourceCodeLocation     // 语句在源码中的位置
)
