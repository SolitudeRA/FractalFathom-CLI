package org.protogalaxy.fractalfathom.cli.modelInference

import kotlinx.coroutines.runBlocking
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRFieldEntity
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRMethodEntity
import java.util.concurrent.TimeUnit

class LLMUtils {

    private val client = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()
    private val mapper = jacksonObjectMapper()
    private val endpointURL = "http://localhost:5000/generate_plantuml"

    private val openaiApiKey = System.getenv("OPENAI_API_KEY")
        ?: throw IllegalStateException("Environment variable OPENAI_API_KEY is not set")

    fun generatePlantUML(irClasses: List<IRClassEntity>): String = runBlocking {
        val prompt = constructPrompt(irClasses)
        val plantUMLCode = callGPT4API(prompt)
        plantUMLCode
    }

    private fun callGPT4API(prompt: String): String {
        val jsonData = mapper.writeValueAsString(
            mapOf(
                "prompt" to prompt,
                "api_key" to openaiApiKey
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
            val responseMap: Map<String, String> = mapper.readValue(responseJson)

            return responseMap["plantuml_code"] ?: throw Exception("No PlantUML code in response")
        }
    }

    fun constructPrompt(irClasses: List<IRClassEntity>): String {
        val promptBuilder = StringBuilder()
        promptBuilder.append("Based on the following code structure data, generate a PlantUML component diagram code that meets the following requirements:\n")

        promptBuilder.append(
            """
            1. The diagram should accurately represent the Java classes, methods, fields, and their relationships (inheritance, implementation, dependency).
            2. Clearly show the functional hierarchy, grouping components according to their features and mappings.
            3. Display annotations and labels for key functional features and business logic mappings.
            4. Ensure the diagram is clear, well-structured, and suitable for rendering and understanding.
            5. Use appropriate PlantUML syntax, so that the output can be directly rendered.
            6. The diagram should be suitable for inclusion in IntelliJ IDEA's PlantUML plugin, and support users clicking components to navigate to code.
            7. Simplify the diagram if necessary to avoid excessive complexity, while ensuring key components and relationships are shown.
            """.trimIndent()
        )

        promptBuilder.append("\nCode Structure Data:\n")

        for (irClass in irClasses) {
            val classInfo = StringBuilder().apply {
                append("Class: ${irClass.name} (Type: ${irClass.type})\n")
                irClass.embedding?.let { append("Embeddings:\n- ${it.values.joinToString(", ")}\n") }
                appendSection("Features", irClass.features.map { feature -> feature.name })
                appendSection("Mappings", irClass.mappings.map { mapping -> mapping.toConcept })
                appendSection(
                    "Relations",
                    irClass.relations.map { relation -> "${relation.relationType}: ${relation.targetClass}" })
                appendFields(irClass.fields)
                appendMethods(irClass.methods)
            }
            promptBuilder.append(classInfo).append("\n")
        }

        promptBuilder.append("\nPlease generate the PlantUML component diagram code accordingly.")
        return promptBuilder.toString()
    }

    private fun StringBuilder.appendSection(title: String, items: List<String>) {
        if (items.isNotEmpty()) {
            append("$title:\n")
            items.forEach { item -> append("- $item\n") }
        }
    }

    private fun StringBuilder.appendFields(fields: List<IRFieldEntity>?) {
        fields?.takeIf { it.isNotEmpty() }?.let {
            append("Fields:\n")
            for (field in it) {
                val modifiers = field.modifiers
                val annotations = field.annotations.joinToString(" ") { annotation -> annotation.name }
                append("- $modifiers ${field.type} ${field.name} $annotations\n")
                appendDetails(field.features.map { it.name }, field.mappings.map { it.toConcept })
            }
        }
    }

    private fun StringBuilder.appendMethods(methods: List<IRMethodEntity>?) {
        methods?.takeIf { it.isNotEmpty() }?.let {
            append("Methods:\n")
            for (method in it) {
                val modifiers = method.modifiers
                val annotations = method.annotations.joinToString(" ") { annotation -> annotation.name }
                val parameters =
                    method.parameters.joinToString(", ") { parameter -> "${parameter.type} ${parameter.name}" }
                append("- $modifiers ${method.returnType} ${method.name}($parameters) $annotations\n")
                appendDetails(method.features.map { it.name }, method.mappings.map { it.toConcept })
            }
        }
    }

    private fun StringBuilder.appendDetails(features: List<String>, mappings: List<String>, embeddings: List<Float>? = null) {
        if (features.isNotEmpty() || mappings.isNotEmpty() || (embeddings?.isNotEmpty() == true)) {
            append("  Details:\n")
            if (features.isNotEmpty()) append("    Features: ${features.joinToString(", ")}\n")
            if (mappings.isNotEmpty()) append("    Mappings: ${mappings.joinToString(", ")}\n")
            embeddings?.let { append("    Embedding: ${it.joinToString(", ")}\n") }
        }
    }
}