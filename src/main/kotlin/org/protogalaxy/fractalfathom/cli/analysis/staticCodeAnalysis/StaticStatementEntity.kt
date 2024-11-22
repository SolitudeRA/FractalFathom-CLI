package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

/**
 * Represents a static statement in the code.
 *
 * @property type The type of the statement (e.g., assignment, conditional statement).
 * @property expression The content of the expression, if any.
 * @property subStatements An optional list of sub-statements for nested structures.
 * @property sourceCodeLocation The location of the statement in the source code.
 */
data class StaticStatementEntity(
    val type: String,                              // 语句类型（如赋值、条件语句等）
    val expression: String?,                       // 表达式内容
    val subStatements: List<StaticStatementEntity>?, // 子语句列表
    val sourceCodeLocation: SourceCodeLocation?     // 语句在源码中的位置
)
