package org.protogalaxy.fractalfathom.cli.modelInference

import kotlinx.coroutines.runBlocking
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.protogalaxy.fractalfathom.cli.analysis.ir.Embedding
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity

class GraphCodeBERTUtils {

    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    private val endpointURL = "http://localhost:5000/generate_embeddings"

    // Function to enhance IR data with embeddings
    fun enhanceIRDataWithEmbeddings(irClasses: List<IRClassEntity>): List<IRClassEntity> = runBlocking {
        irClasses.map { irClass ->
            // Prepare code snippets for the class, fields, and methods
            val entityList = mutableListOf<Pair<String, String>>()

            // Add class
            val classId = "class_${irClass.name}"
            val classText = irClass.toTextFormat()
            entityList.add(classId to classText)

            // Add fields
            irClass.fields.forEach { field ->
                val fieldId = "field_${irClass.name}_${field.name}"
                val fieldText = "${field.name}: ${field.type}"
                entityList.add(fieldId to fieldText)
            }

            // Add methods
            irClass.methods.forEach { method ->
                val methodId = "method_${irClass.name}_${method.name}"
                val methodText = method.toTextFormat()
                entityList.add(methodId to methodText)
            }

            // Call the API
            val embeddingsMap = callGraphCodeBERTAPI(entityList)

            // Embed the embeddings into IR structures
            val enhancedFields = irClass.fields.map { field ->
                val fieldId = "field_${irClass.name}_${field.name}"
                val embeddingValues = embeddingsMap[fieldId] ?: emptyList()
                field.copy(embedding = Embedding(embeddingValues))
            }

            val enhancedMethods = irClass.methods.map { method ->
                val methodId = "method_${irClass.name}_${method.name}"
                val embeddingValues = embeddingsMap[methodId] ?: emptyList()
                method.copy(embedding = Embedding(embeddingValues))
            }

            val classEmbeddingValues = embeddingsMap[classId] ?: emptyList()
            irClass.copy(
                fields = enhancedFields,
                methods = enhancedMethods,
                embedding = Embedding(classEmbeddingValues)
            )
        }
    }

    // Function to call the GraphCodeBERT API
    private fun callGraphCodeBERTAPI(entities: List<Pair<String, String>>): Map<String, List<Double>> {
        val jsonData = mapper.writeValueAsString(mapOf(
            "ir_entities" to entities.map { (id, code) -> mapOf("id" to id, "code_snippet" to code) }
        ))
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

    // Extension functions to convert IR entities to text format
    private fun IRClassEntity.toTextFormat(): String {
        val fieldsText = fields.joinToString(", ") { "${it.name}: ${it.type}" }
        val methodsText = methods.joinToString(", ") { it.toTextFormat() }
        val featureText = features.joinToString(", ") { it.name }
        val mappingText = mappings.joinToString(", ") { it.toConcept }
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

    private fun IRMethodEntity.toTextFormat(): String {
        val paramsText = parameters.joinToString(", ") { "${it.name}: ${it.type}" }

        return """
            Method: $name
            ReturnType: $returnType
            Parameters: $paramsText
            Annotations: ${annotations.joinToString(", ") { it.name }}
            Features: ${features.joinToString(", ") { it.name }}
            Mappings: ${mappings.joinToString(", ") { it.toConcept }}
            CalledMethods: ${calledMethods.joinToString(", ") { it.methodName }}
        """.trimIndent()
    }
}