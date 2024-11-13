package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

data class LowLevelAST(
    val statements: List<StaticStatementEntity>    // 方法体的低层AST
)
