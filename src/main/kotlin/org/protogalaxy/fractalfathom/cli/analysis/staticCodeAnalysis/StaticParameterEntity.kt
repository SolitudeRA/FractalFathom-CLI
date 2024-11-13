package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

data class StaticParameterEntity(
    val name: String,                              // 参数名称
    val type: String,                              // 参数类型
    val sourceCodeLocation: SourceCodeLocation     // 参数在源码中的位置
)
