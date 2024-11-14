package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationPhase
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationTargetType
import spoon.reflect.declaration.*

/**
 * 注解解析器，解析注解信息。
 */
class AnnotationParser {

    /**
     * 解析 CtAnnotation 对象，生成 AnnotationEntity。
     *
     * @param ctAnnotation Spoon 中的 CtAnnotation 对象。
     * @return 解析得到的 AnnotationEntity。
     */
    fun parseAnnotation(ctAnnotation: CtAnnotation<*>): AnnotationEntity {
        val name = ctAnnotation.annotationType.qualifiedName

        // 解析属性
        val attributes = ctAnnotation.values.mapValues {
            if (it.key == "type") {
                it.value.toString().replaceBeforeLast(".", "").removeSurrounding(".", "")
            } else {
                it.value.toString().removeSurrounding("\"", "\"")
            }
        }

        // 注解目标元素及类型
        val targetElement = ctAnnotation.parent?.toString() ?: "Unknown"
        val targetType = when (ctAnnotation.annotatedElementType) {
            CtAnnotatedElementType.TYPE -> AnnotationTargetType.CLASS
            CtAnnotatedElementType.FIELD -> AnnotationTargetType.FIELD
            CtAnnotatedElementType.METHOD -> AnnotationTargetType.METHOD
            else -> AnnotationTargetType.CLASS
        }

        // 源码位置
        val sourceCodeLocation = ctAnnotation.position?.let {
            SourceCodeLocation(
                filePath = it.file?.path ?: "",
                startLine = it.line,
                endLine = it.endLine,
                startColumn = it.column,
                endColumn = it.endColumn
            )
        } ?: SourceCodeLocation("", -1, -1, -1, -1)

        return AnnotationEntity(
            name = name,
            attributes = attributes,
            targetElement = targetElement,
            targetType = targetType,
            condition = null, // 激活条件待解析
            dependencies = null, // 依赖的注解待解析
            phase = AnnotationPhase.RUNTIME, // 生效阶段默认 RUNTIME
            sourceCodeLocation = sourceCodeLocation
        )
    }
}