package org.protogalaxy.fractalfathom.cli.analysis.parsers


import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import spoon.reflect.declaration.CtMethod

class MethodParser {

    /**
     * 解析 CtMethod 对象，生成 IRMethodEntity。
     *
     * @param ctMethod Spoon 中的 CtMethod 对象。
     * @return 解析得到的 IRMethodEntity。
     */
    fun parseMethod(ctMethod: CtMethod<*>): IRMethodEntity {
        val name = ctMethod.simpleName
        val returnType = ctMethod.type.qualifiedName
        val modifiers = ctMethod.modifiers.joinToString(" ")

        // 解析参数
        val parameterParser = ParameterParser()
        val parameters = ctMethod.parameters.map {
            parameterParser.parseParameter(it)
        }

        // 解析注解
        val annotationParser = AnnotationParser()
        val annotations = ctMethod.annotations.map {
            annotationParser.parseAnnotation(it)
        }

        // 解析方法体 AST
        val astParser = ASTParser()
        val lowLevelAST = astParser.parseAST(ctMethod)

        // 源码位置
        val sourceCodeLocation = SourceCodeLocation(
            filePath = ctMethod.position?.file?.path ?: "",
            startLine = ctMethod.position?.line ?: 0,
            endLine = ctMethod.position?.endLine ?: 0,
            startColumn = ctMethod.position?.column ?: 0,
            endColumn = ctMethod.position?.endColumn ?: 0
        )

        return IRMethodEntity(
            name = name,
            returnType = returnType,
            parameters = parameters,
            modifiers = modifiers,
            annotations = annotations,
            features = emptyList(), // 功能特性待解析
            mappings = emptyList(), // 业务逻辑映射待解析
            calledMethods = emptyList(), // 调用关系待解析
            lowLevelAST = lowLevelAST,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}