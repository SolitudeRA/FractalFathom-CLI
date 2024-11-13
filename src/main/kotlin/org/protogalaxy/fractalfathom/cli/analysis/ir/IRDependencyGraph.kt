package org.protogalaxy.fractalfathom.cli.analysis.ir

data class IRDependencyGraph(
    val dependencies: List<IRDependencyEdge>       // 系统级依赖关系
)
