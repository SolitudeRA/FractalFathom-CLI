package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.LowLevelAST
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticStatementEntity
import spoon.reflect.code.CtStatement
import spoon.reflect.declaration.CtExecutable

/**
 * AST 解析器，解析方法体的低层 AST。
 */
class ASTParser {

    /**
     * 解析 CtExecutable 的方法体，生成 LowLevelAST。
     *
     * @param ctExecutable Spoon 中的 CtExecutable 对象。
     * @return 解析得到的 LowLevelAST。
     */
    fun parseAST(ctExecutable: CtExecutable<*>): LowLevelAST? {
        val body = ctExecutable.body ?: return null
        val statements = body.statements.map { parseStatement(it) }
        return LowLevelAST(statements)
    }


    /**
     * 递归解析 CtStatement，生成 StaticStatementEntity。
     *
     * @param ctStatement Spoon 中的 CtStatement 对象。
     * @return 解析得到的 StaticStatementEntity。
     */
    private fun parseStatement(ctStatement: CtStatement): StaticStatementEntity {
        val type = ctStatement.javaClass.simpleName
        val expression = ctStatement.toString()

        // 解析子语句
        val subStatements = if (ctStatement is spoon.reflect.code.CtBlock<*>) {
            ctStatement.statements.map { parseStatement(it) }
        } else {
            null
        }

        // 源码位置
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