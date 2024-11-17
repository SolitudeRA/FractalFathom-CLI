package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticRelationEntity
import spoon.reflect.declaration.CtClass

/**
 * A parser for converting Spoon's `CtClass` representation of a Java class into
 * a custom `IRClassEntity` representation, including annotations, fields, methods,
 * and relationships.
 */
class ClassParser {

    /**
     * Parses a `CtClass` object into a custom `IRClassEntity`.
     *
     * @param ctClass The Spoon `CtClass` object representing a Java class or interface.
     * @param filePath The file path of the Java source file containing this class.
     * @return An `IRClassEntity` containing metadata, fields, methods, annotations, and relationships.
     */
    fun parseClass(ctClass: CtClass<*>, filePath: String): IRClassEntity {
        val name = ctClass.simpleName
        val type = if (ctClass.isInterface) "Interface" else "Class"
        val packageName = ctClass.`package`?.qualifiedName ?: ""
        val modifiers = ctClass.modifiers.joinToString(" ")
        val superClass = ctClass.superclass?.qualifiedName
        val interfaces = ctClass.superInterfaces.map { it.qualifiedName }

        val annotationParser = AnnotationParser()
        val annotations = ctClass.annotations.map {
            annotationParser.parseAnnotation(it)
        }

        val fieldParser = FieldParser()
        val fields = ctClass.fields.map {
            fieldParser.parseField(it)
        }

        val methodParser = MethodParser()
        val methods = ctClass.methods.map {
            methodParser.parseMethod(it)
        }

        // Collect class relationships (e.g., inheritance, implementation)
        val relations = mutableListOf<StaticRelationEntity>()
        superClass?.let {
            relations.add(StaticRelationEntity("extends", it))
        }
        interfaces.forEach {
            relations.add(StaticRelationEntity("implements", it))
        }

        val sourceCodeLocation = SourceCodeLocation(
            filePath = filePath,
            startLine = ctClass.position?.line ?: 0,
            endLine = ctClass.position?.endLine ?: 0,
            startColumn = ctClass.position?.column ?: 0,
            endColumn = ctClass.position?.endColumn ?: 0
        )

        // Calculate complexity metrics for the class
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
            features = emptyList(),
            mappings = emptyList(),
            fields = fields,
            methods = methods,
            relations = relations,
            sourceCodeLocation = sourceCodeLocation,
            complexityMetrics = complexityMetrics
        )
    }
}