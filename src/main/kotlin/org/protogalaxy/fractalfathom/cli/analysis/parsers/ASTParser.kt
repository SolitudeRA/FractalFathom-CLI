package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.LowLevelAST
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticStatementEntity
import spoon.reflect.code.CtStatement
import spoon.reflect.declaration.CtExecutable

/**
 * A parser for analyzing and extracting low-level AST (Abstract Syntax Tree) information
 * from Java executable elements (e.g., methods, constructors).
 *
 * This class processes Spoon's `CtExecutable` and its statements to generate
 * a custom low-level AST representation, suitable for further analysis.
 */
class ASTParser {

    /**
     * Parses the body of a given `CtExecutable` (e.g., a method or constructor) into a low-level AST.
     *
     * @param ctExecutable The Spoon `CtExecutable` object to parse.
     * @return A `LowLevelAST` object containing the parsed statements, or `null` if the body is empty.
     */
    fun parseAST(ctExecutable: CtExecutable<*>): LowLevelAST? {
        val body = ctExecutable.body ?: return null
        val statements = body.statements.map { parseStatement(it) }
        return LowLevelAST(statements)
    }

    /**
     * Parses a single `CtStatement` into a custom `StaticStatementEntity`.
     *
     * This method handles different types of statements and recursively processes
     * block statements (e.g., `{ ... }`).
     *
     * @param ctStatement The Spoon `CtStatement` to parse.
     * @return A `StaticStatementEntity` representing the statement.
     */
    private fun parseStatement(ctStatement: CtStatement): StaticStatementEntity {
        val type = ctStatement.javaClass.simpleName
        val expression = ctStatement.toString()

        val subStatements = if (ctStatement is spoon.reflect.code.CtBlock<*>) {
            ctStatement.statements.map { parseStatement(it) }
        } else {
            null
        }

        val sourceCodeLocation = ctStatement.position?.let {
            SourceCodeLocation(
                filePath = it.file?.path ?: "",
                startLine = it.line,
                endLine = it.endLine,
                startColumn = it.column,
                endColumn = it.endColumn
            )
        } ?: SourceCodeLocation("", 0, 0, 0, 0)

        return StaticStatementEntity(
            type = type,
            expression = expression,
            subStatements = subStatements,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}