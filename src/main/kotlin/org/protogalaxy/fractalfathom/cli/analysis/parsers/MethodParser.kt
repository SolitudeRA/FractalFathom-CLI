package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import spoon.reflect.declaration.CtMethod

/**
 * A parser for converting Spoon's `CtMethod` representation of a Java method
 * into a custom `IRMethodEntity` representation.
 */
class MethodParser {

    /**
     * Parses a Spoon `CtMethod` object into a custom `IRMethodEntity`.
     *
     * @param ctMethod The Spoon `CtMethod` object representing a Java method.
     * @return An `IRMethodEntity` containing the method's metadata, parameters, annotations,
     * and a low-level AST representation of its body.
     */
    fun parseMethod(ctMethod: CtMethod<*>): IRMethodEntity {
        val name = ctMethod.simpleName
        val returnType = ctMethod.type.qualifiedName
        val modifiers = ctMethod.modifiers.joinToString(" ")

        val parameterParser = ParameterParser()
        val parameters = ctMethod.parameters.map {
            parameterParser.parseParameter(it)
        }

        val annotationParser = AnnotationParser()
        val annotations = ctMethod.annotations.map {
            annotationParser.parseAnnotation(it)
        }

        val astParser = ASTParser()
        val lowLevelAST = astParser.parseAST(ctMethod)

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
            features = emptyList(),
            mappings = emptyList(),
            calledMethods = emptyList(),
            lowLevelAST = lowLevelAST,
            sourceCodeLocation = sourceCodeLocation
        )
    }
}