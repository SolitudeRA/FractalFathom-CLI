package org.protogalaxy.fractalfathom.cli.analysis.parsers


import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticParameterEntity
import spoon.reflect.declaration.CtParameter

/**
 * A parser for converting Spoon's `CtParameter` representation of a method parameter
 * into a custom `StaticParameterEntity` representation.
 */
class ParameterParser {

    /**
     * Parses a Spoon `CtParameter` object into a custom `StaticParameterEntity`.
     *
     * @param ctParameter The Spoon `CtParameter` object representing a method parameter.
     * @return A `StaticParameterEntity` containing the parameter's name, type, and source code location.
     */
    fun parseParameter(ctParameter: CtParameter<*>): StaticParameterEntity {
        val name = ctParameter.simpleName
        val type = ctParameter.type.qualifiedName

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