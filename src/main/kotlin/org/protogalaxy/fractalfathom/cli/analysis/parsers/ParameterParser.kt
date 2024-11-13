package org.protogalaxy.fractalfathom.cli.analysis.parsers


import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticParameterEntity
import spoon.reflect.declaration.CtParameter

/**
 * 参数解析器，解析方法参数信息。
 */
class ParameterParser {

    /**
     * 解析 CtParameter 对象，生成 StaticParameterEntity。
     *
     * @param ctParameter Spoon 中的 CtParameter 对象。
     * @return 解析得到的 StaticParameterEntity。
     */
    fun parseParameter(ctParameter: CtParameter<*>): StaticParameterEntity {
        val name = ctParameter.simpleName
        val type = ctParameter.type.qualifiedName

        // 源码位置
        val sourceCodeLocation = ctParameter.position?.let {
            SourceCodeLocation(
                filePath = it.file?.path ?: "",
                startLine = it.line,
                endLine = it.endLine,
                startColumn = it.column,
                endColumn = it.endColumn
            )
        } ?: SourceCodeLocation("", 0, 0, 0, 0)

        return StaticParameterEntity(
            name = name,
            type = type,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}