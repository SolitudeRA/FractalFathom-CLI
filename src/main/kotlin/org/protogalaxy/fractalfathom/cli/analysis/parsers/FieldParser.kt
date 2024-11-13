package org.protogalaxy.fractalfathom.cli.analysis.parsers


import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import spoon.reflect.declaration.CtField

class FieldParser {

    /**
     * 解析 CtField 对象，生成 IRFieldEntity。
     *
     * @param ctField Spoon 中的 CtField 对象。
     * @return 解析得到的 IRFieldEntity。
     */
    fun parseField(ctField: CtField<*>): IRFieldEntity {
        val name = ctField.simpleName
        val type = ctField.type.qualifiedName
        val modifiers = ctField.modifiers.joinToString(" ")

        // 解析注解
        val annotationParser = AnnotationParser()
        val annotations = ctField.annotations.map {
            annotationParser.parseAnnotation(it)
        }

        // 源码位置
        val sourceCodeLocation = SourceCodeLocation(
            filePath = ctField.position?.file?.path ?: "",
            startLine = ctField.position?.line ?: 0,
            endLine = ctField.position?.endLine ?: 0,
            startColumn = ctField.position?.column ?: 0,
            endColumn = ctField.position?.endColumn ?: 0
        )

        return IRFieldEntity(
            name = name,
            type = type,
            modifiers = modifiers,
            annotations = annotations,
            features = emptyList(), // 功能特性待解析
            mappings = emptyList(), // 业务逻辑映射待解析
            sourceCodeLocation = sourceCodeLocation
        )
    }
}