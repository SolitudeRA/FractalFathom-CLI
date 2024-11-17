package org.protogalaxy.fractalfathom.cli.modelInference

import kotlinx.coroutines.*
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.protogalaxy.fractalfathom.cli.analysis.annotation.AnnotationEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.Embedding
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity

/**
 * A utility class to enhance IR data with embeddings generated via the GraphCodeBERT API.
 */
class GraphCodeBERTUtils {

    private val client = OkHttpClient() // HTTP client for API calls
    private val mapper = jacksonObjectMapper() // JSON serializer/deserializer
    private val endpointURL = "http://localhost:5000/generate_embeddings" // API endpoint URL
    private val batchSize = 10 // Number of entities processed per batch

    /**
     * Enhance IR data by generating embeddings for classes, fields, and methods.
     *
     * @param irClasses List of IRClassEntity to enhance.
     * @return List of IRClassEntity with embedded data.
     */
    fun enhanceIRDataWithEmbeddings(irClasses: List<IRClassEntity>): List<IRClassEntity> = runBlocking {
        irClasses.map { irClass ->
            // Prepare a list of entities (class, fields, methods) with their associated code snippets
            val entityList = mutableListOf<Pair<String, String>>()

            // Add class-level entities
            val classId = "class_${irClass.name}"
            val classText = irClass.toTextFormat()
            val classContext = generateClassContext(irClass)
            entityList.add(classId to mergeSnippetWithContext(classText, classContext))

            // Add field-level entities
            irClass.fields.forEach { field ->
                val fieldId = "field_${irClass.name}_${field.name}"
                val fieldText = "${field.name}: ${field.type}"
                val fieldContext = generateFieldContext(field)
                entityList.add(fieldId to mergeSnippetWithContext(fieldText, fieldContext))
            }

            // Add method-level entities
            irClass.methods.forEach { method ->
                val methodId = "method_${irClass.name}_${method.name}"
                val methodText = method.toTextFormat()
                val methodContext = generateMethodContext(method)
                entityList.add(methodId to mergeSnippetWithContext(methodText, methodContext))
            }

            // Call the GraphCodeBERT API in batches to generate embeddings
            val embeddingsMap = callGraphCodeBERTAPIInBatches(entityList)

            // Update fields with embeddings
            val enhancedFields = irClass.fields.map { field ->
                val fieldId = "field_${irClass.name}_${field.name}"
                val embeddingValues = embeddingsMap[fieldId] ?: emptyList()
                field.copy(embedding = Embedding(embeddingValues))
            }

            // Update methods with embeddings
            val enhancedMethods = irClass.methods.map { method ->
                val methodId = "method_${irClass.name}_${method.name}"
                val embeddingValues = embeddingsMap[methodId] ?: emptyList()
                method.copy(embedding = Embedding(embeddingValues))
            }

            // Update class with embeddings
            val classEmbeddingValues = embeddingsMap[classId] ?: emptyList()
            irClass.copy(
                fields = enhancedFields,
                methods = enhancedMethods,
                embedding = Embedding(classEmbeddingValues)
            )
        }
    }

    /**
     * Perform concurrent and batched calls to the GraphCodeBERT API to generate embeddings.
     *
     * @param entities List of entities with IDs and code snippets.
     * @return Map of entity IDs to their embedding vectors.
     */
    private suspend fun callGraphCodeBERTAPIInBatches(entities: List<Pair<String, String>>): Map<String, List<Double>> {
        val entityBatches = entities.chunked(batchSize) // Split entities into batches

        return coroutineScope {
            val deferredResults = entityBatches.map { batch ->
                async {
                    callGraphCodeBERTAPIBatch(batch)
                }
            }

            // Wait for all API calls to complete and merge their results
            deferredResults.awaitAll()
                .flatMap { it.entries }
                .associate { it.key to it.value }
        }
    }

    /**
     * Make a single batch API call to generate embeddings.
     *
     * @param batch A batch of entities with IDs and code snippets.
     * @return Map of entity IDs to their embedding vectors.
     */
    private fun callGraphCodeBERTAPIBatch(batch: List<Pair<String, String>>): Map<String, List<Double>> {
        val jsonData = mapper.writeValueAsString(
            mapOf(
                "ir_entities" to batch.map { (id, code) ->
                    mapOf(
                        "id" to id,
                        "code_snippet" to code
                    )
                }
            )
        )
        val requestBody = jsonData.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(endpointURL)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Unexpected code $response")

            val responseJson = response.body?.string() ?: throw Exception("Empty response body")
            val responseMap: Map<String, Map<String, List<Double>>> = mapper.readValue(responseJson)

            return responseMap["embeddings"] ?: throw Exception("Invalid response format")
        }
    }

    /**
     * Merge a code snippet with its context for better semantic understanding.
     *
     * @param snippet The code snippet.
     * @param context The context information.
     * @return The combined snippet with context.
     */
    private fun mergeSnippetWithContext(snippet: String, context: String): String {
        val contextHeader = "// Context Information:\n"
        return "$contextHeader$context\n\n$snippet"
    }

    // Generate the context for a class entity
    private fun generateClassContext(irClass: IRClassEntity): String {
        return generateEntityContext(irClass.annotations, irClass.features, irClass.mappings)
    }

    // Generate the context for a field entity
    private fun generateFieldContext(field: IRFieldEntity): String {
        return generateEntityContext(field.annotations, field.features, field.mappings)
    }

    // Generate the context for a method entity
    private fun generateMethodContext(method: IRMethodEntity): String {
        return generateEntityContext(method.annotations, method.features, method.mappings)
    }

    /**
     * Generate context information from annotations, features, and mappings.
     *
     * @param annotations List of annotations.
     * @param features List of features.
     * @param mappings List of mappings.
     * @return Context information as a string.
     */
    private fun generateEntityContext(annotations: List<AnnotationEntity>, features: List<FeatureEntity>, mappings: List<MappingEntity>): String {
        return buildString {
            append("Annotations: ${annotations.joinToString(", ") { it.name }}\n")
            append("Features: ${features.joinToString(", ") { it.name }}\n")
            append("Mappings: ${mappings.joinToString(", ") { it.toConcept }}\n")
        }
    }

    // Extension functions to convert IR entities to text format
    private fun IRClassEntity.toTextFormat(): String {
        val fieldsText = fields.joinToString(", ") { "${it.name}: ${it.type}" }
        val methodsText = methods.joinToString(", ") { it.toTextFormat() }
        val featureText = features.joinToString(", ") { "${it.name}: ${it.description ?: "No description"}" }
        val mappingText = mappings.joinToString(", ") { "${it.toConcept} (${it.type})" }
        val annotationsText = annotations.joinToString(", ") { it.name }

        return """
            Class: $name
            Type: $type
            Package: $packageName
            Superclass: ${superClass ?: "None"}
            Interfaces: ${interfaces.joinToString(", ")}
            Annotations: $annotationsText
            Features: $featureText
            Mappings: $mappingText
            Fields: $fieldsText
            Methods: $methodsText
        """.trimIndent()
    }

    // Extension function to convert IRMethodEntity to text format
    private fun IRMethodEntity.toTextFormat(): String {
        val paramsText = parameters.joinToString(", ") { "${it.name}: ${it.type}" }
        val featureText = features.joinToString(", ") { "${it.name}: ${it.description ?: "No description"}" }
        val mappingText = mappings.joinToString(", ") { "${it.toConcept} (${it.type})" }

        return """
            Method: $name
            ReturnType: $returnType
            Parameters: $paramsText
            Annotations: ${annotations.joinToString(", ") { it.name }}
            Features: $featureText
            Mappings: $mappingText
            CalledMethods: ${calledMethods.joinToString(", ") { it.methodName }}
        """.trimIndent()
    }
}