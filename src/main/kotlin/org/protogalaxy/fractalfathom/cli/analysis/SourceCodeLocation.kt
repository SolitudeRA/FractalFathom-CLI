package org.protogalaxy.fractalfathom.cli.analysis

/**
 * Represents the location of a code element in the source code.
 *
 * @property filePath The path of the file where the code element is located.
 * @property startLine The starting line number of the code element.
 * @property endLine The ending line number of the code element.
 * @property startColumn The starting column number of the code element.
 * @property endColumn The ending column number of the code element.
 */
data class SourceCodeLocation(
    var filePath: String,            // 文件路径
    var startLine: Int,              // 起始行号
    var endLine: Int,                // 结束行号
    var startColumn: Int,            // 起始列号
    var endColumn: Int               // 结束列号
)