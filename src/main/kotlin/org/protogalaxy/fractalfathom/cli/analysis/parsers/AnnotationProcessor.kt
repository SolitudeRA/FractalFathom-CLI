package org.protogalaxy.fractalfathom.cli.analysis.parsers

import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureType
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingType

class AnnotationProcessor {

    /**
     * Processes a list of annotations and classifies them into features, mappings, and regular annotations.
     *
     * @param annotations List of `AnnotationEntity` to be processed.
     * @return A Triple containing lists of features, mappings, and regular annotations.
     */
    fun processAnnotations(annotations: List<AnnotationEntity>): Triple<List<FeatureEntity>, List<MappingEntity>, List<AnnotationEntity>> {
        val features = mutableListOf<FeatureEntity>()
        val mappings = mutableListOf<MappingEntity>()
        val regularAnnotations = mutableListOf<AnnotationEntity>()

        annotations.forEach { annotation ->
            when (annotation.name) {
                "org.protogalaxy.fractalfathom.FractalFathomFeature" -> {
                    features.add(
                        FeatureEntity(
                            name = annotation.attributes["name"] as? String ?: "Unnamed Feature",
                            description = annotation.attributes["description"] as? String,
                            type = FeatureType.valueOf(annotation.attributes["type"] as? String ?: "FUNCTIONAL")
                        )
                    )
                }
                "org.protogalaxy.fractalfathom.FractalFathomMapping" -> {
                    mappings.add(
                        MappingEntity(
                            toConcept = annotation.attributes["toConcept"] as? String ?: "Unnamed Mapping",
                            type = MappingType.valueOf(annotation.attributes["type"] as? String ?: "CONCEPT")
                        )
                    )
                }
                else -> {
                    regularAnnotations.add(annotation)
                }
            }
        }

        return Triple(features, mappings, regularAnnotations)
    }
}