package org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation

/**
 * Represents a static method call in the code.
 *
 * @property methodName The name of the method being called.
 * @property arguments The arguments passed during the method call.
 * @property sourceCodeLocation The location in the source code where the call occurs.
 */
data class StaticCallEntity(
    val methodName: String,                        // 被调用方法的名称
    val arguments: List<String>,                   // 调用时传入的参数
    val sourceCodeLocation: SourceCodeLocation?     // 调用发生的源码位置
)
