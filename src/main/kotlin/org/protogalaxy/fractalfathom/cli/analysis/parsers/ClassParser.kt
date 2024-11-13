package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticRelationEntity
import spoon.reflect.declaration.CtClass

/**
 * 类解析器，解析类的结构信息。
 */
class ClassParser {

    /**
     * 解析 CtClass 对象，生成 IRClassEntity。
     *
     * @param ctClass Spoon 中的 CtClass 对象。
     * @param filePath 文件路径。
     * @return 解析得到的 IRClassEntity。
     */
    fun parseClass(ctClass: CtClass<*>, filePath: String): IRClassEntity {
        val name = ctClass.simpleName
        val type = if (ctClass.isInterface) "Interface" else "Class"
        val packageName = ctClass.`package`?.qualifiedName ?: ""
        val modifiers = ctClass.modifiers.joinToString(" ")
        val superClass = ctClass.superclass?.qualifiedName
        val interfaces = ctClass.superInterfaces.map { it.qualifiedName }

        // 解析注解
        val annotationParser = AnnotationParser()
        val annotations = ctClass.annotations.map {
            annotationParser.parseAnnotation(it)
        }

        // 解析字段
        val fieldParser = FieldParser()
        val fields = ctClass.fields.map {
            fieldParser.parseField(it)
        }

        // 解析方法
        val methodParser = MethodParser()
        val methods = ctClass.methods.map {
            methodParser.parseMethod(it)
        }

        // 解析继承和实现关系
        val relations = mutableListOf<StaticRelationEntity>()
        superClass?.let {
            relations.add(StaticRelationEntity("extends", it))
        }
        interfaces.forEach {
            relations.add(StaticRelationEntity("implements", it))
        }

        // 源码位置
        val sourceCodeLocation = SourceCodeLocation(
            filePath = filePath,
            startLine = ctClass.position?.line ?: 0,
            endLine = ctClass.position?.endLine ?: 0,
            startColumn = ctClass.position?.column ?: 0,
            endColumn = ctClass.position?.endColumn ?: 0
        )

        // 计算复杂度指标
        val complexityMetricsCalculator = ComplexityMetricsCalculator()
        val complexityMetrics = complexityMetricsCalculator.calculateComplexity(ctClass)

        return IRClassEntity(
            name = name,
            type = type,
            packageName = packageName,
            filePath = filePath,
            modifiers = modifiers,
            superClass = superClass,
            interfaces = interfaces,
            annotations = annotations,
            features = emptyList(), // 功能特性待解析
            mappings = emptyList(), // 业务逻辑映射待解析
            fields = fields,
            methods = methods,
            relations = relations,
            sourceCodeLocation = sourceCodeLocation,
            complexityMetrics = complexityMetrics
        )
    }
}