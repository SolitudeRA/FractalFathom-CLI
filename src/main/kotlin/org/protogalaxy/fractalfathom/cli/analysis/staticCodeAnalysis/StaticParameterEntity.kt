package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

/**
 * Represents a parameter in a method signature.
 *
 * @property name The name of the parameter.
 * @property type The data type of the parameter.
 * @property sourceCodeLocation The location of the parameter in the source code.
 */
data class StaticParameterEntity(
    val name: String,                              // 参数名称
    val type: String,                              // 参数类型
    val sourceCodeLocation: SourceCodeLocation     // 参数在源码中的位置
)
