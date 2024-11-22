package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticRelationEntity

/**
 * Represents a class or interface entity in the intermediate representation (IR).
 *
 * @property name The name of the class or interface.
 * @property type The type of the class (e.g., "Class", "Interface").
 * @property packageName The name of the package containing the class.
 * @property filePath The file path where the class is defined.
 * @property modifiers The modifiers applied to the class (e.g., public, abstract).
 * @property superClass The name of the superclass, if any.
 * @property interfaces A list of interfaces implemented by the class.
 * @property annotations A list of annotations applied to the class.
 * @property features A list of feature entities associated with the class.
 * @property mappings A list of business logic mappings related to the class.
 * @property fields A list of fields defined in the class.
 * @property methods A list of methods defined in the class.
 * @property relations A list of relationships (e.g., inheritance, implementation) associated with the class.
 * @property sourceCodeLocation The location of the class in the source code.
 * @property complexityMetrics The complexity metrics for the class.
 * @property embedding The embedding vector for the class, optional.
 */

data class IRClassEntity(
    val name: String,
    val type: String,
    val packageName: String,
    val filePath: String,
    val modifiers: String,
    val superClass: String?,
    val interfaces: List<String>,
    val annotations: List<AnnotationEntity>,
    val features: List<FeatureEntity>,
    val mappings: List<MappingEntity>,
    val fields: List<IRFieldEntity>,
    val methods: List<IRMethodEntity>,
    val relations: List<StaticRelationEntity>,
    val sourceCodeLocation: SourceCodeLocation?,
    val complexityMetrics: ComplexityMetrics,
    val embedding: Embedding? = null
)
