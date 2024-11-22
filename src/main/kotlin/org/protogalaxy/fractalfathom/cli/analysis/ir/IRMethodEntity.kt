package org.protogalaxy.fractalfathom.cli.analysis.ir

import org.protogalaxy.fractalfathom.cli.analysis.SourceCodeLocation
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.LowLevelAST
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticCallEntity
import org.protogalaxy.fractalfathom.cli.analysis.staticCodeAnalysis.StaticParameterEntity

/**
 * Represents a method entity in the intermediate representation (IR).
 *
 * @property name The name of the method.
 * @property returnType The return type of the method.
 * @property parameters A list of parameters for the method.
 * @property modifiers The modifiers applied to the method (e.g., public, synchronized).
 * @property annotations A list of annotations applied to the method.
 * @property features A list of feature entities associated with the method.
 * @property mappings A list of business logic mappings related to the method.
 * @property calledMethods A list of methods called within the body of this method.
 * @property lowLevelAST The low-level abstract syntax tree of the method body, optional.
 * @property sourceCodeLocation The location of the method in the source code.
 * @property embedding The embedding vector for the method, optional.
 */
data class IRMethodEntity(
    val name: String,
    val returnType: String,
    val parameters: List<StaticParameterEntity>,
    val modifiers: String,
    val annotations: List<AnnotationEntity>,
    val features: List<FeatureEntity>,
    val mappings: List<MappingEntity>,
    val calledMethods: List<StaticCallEntity>,
    val lowLevelAST: LowLevelAST?,
    val sourceCodeLocation: SourceCodeLocation?,
    val embedding: Embedding? = null
)
