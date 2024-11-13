package org.protogalaxy.fractalfathom.cli.analysis

data class SourceCodeLocation(
    var filePath: String,            // 文件路径
    var startLine: Int,              // 起始行号
    var endLine: Int,                // 结束行号
    var startColumn: Int,            // 起始列号
    var endColumn: Int               // 结束列号
)