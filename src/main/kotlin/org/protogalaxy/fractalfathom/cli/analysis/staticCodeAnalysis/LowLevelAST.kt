package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

/**
 * Represents the low-level Abstract Syntax Tree (AST) of a method body.
 *
 * @property statements A list of static statements that make up the method body.
 */
data class LowLevelAST(
    val statements: List<StaticStatementEntity>    // 方法体的低层AST
)
