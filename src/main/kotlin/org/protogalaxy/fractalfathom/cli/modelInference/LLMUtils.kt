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

/**
 * Utility class for interacting with GPT-4 API to generate PlantUML diagrams
 * based on enhanced intermediate representation (IR) of Java code structures.
 */
class LLMUtils {

    // Configure the HTTP client with extended timeouts to handle long API calls
    private val client = OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()
    private val mapper = jacksonObjectMapper() // JSON serializer/deserializer
    private val endpointURL = "http://localhost:5000/generate_plantuml" // API endpoint

    // Retrieve OpenAI API key from environment variables
    private val openaiApiKey = System.getenv("OPENAI_API_KEY")
        ?: throw IllegalStateException("Environment variable OPENAI_API_KEY is not set")

    /**
     * Generates PlantUML code for the given list of IRClassEntity objects.
     *
     * @param irClasses List of IRClassEntity containing code structure data.
     * @return A String containing the generated PlantUML code.
     */
    fun generatePlantUML(irClasses: List<IRClassEntity>): String = runBlocking {
        val prompt = constructPrompt(irClasses)
        val plantUMLCode = callOpenAIAPI(prompt)
        plantUMLCode
    }

    /**
     * Sends the prompt to Open AI API and retrieves the PlantUML code.
     *
     * @param prompt The prompt string constructed from IR data.
     * @return A String containing the generated PlantUML code.
     * @throws Exception if the API response is invalid or an error occurs.
     */
    private fun callOpenAIAPI(prompt: String): String {
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

    /**
     * Constructs a detailed prompt for GPT-4 based on the provided IR data.
     *
     * @param irClasses List of IRClassEntity objects to include in the prompt.
     * @return A String containing the prompt formatted for GPT-4.
     */
    fun constructPrompt(irClasses: List<IRClassEntity>): String {
        val promptBuilder = StringBuilder()
        promptBuilder.append("Based on the following code structure data, generate a PlantUML component diagram code that meets the following requirements:\n")

        promptBuilder.append(
            """
            1. Use `class` notation to represent Java classes, including their fields and methods. Each class should include:
                - Public fields, displayed with a `+` sign and followed by the field type and name in the format `+ fieldName: Type`.
                - Public methods, displayed with a `+` sign and followed by the method name, parameters, and return type in the format `+ methodName(param: Type): ReturnType`.

            2. Use `package` notation to group related classes. For example, place all classes related to "User Management" in a package labeled "User Management".
            3. Show relationships between classes, including:
                - Dependency relationships using arrows, e.g., `ClassA --> ClassB : relationship`.
                - If a class uses another class, display it as a `uses` relationship.
                - If a class manages another class, display it as a `manages` relationship.
            4. Omit complex details like embeddings and annotation-specific classes, and avoid displaying annotation fields or lifecycle phases directly. Focus only on functional relationships and hierarchies.
            5. Use simple PlantUML syntax that is compatible with IntelliJ IDEA's PlantUML plugin to ensure the code can be rendered without syntax errors.
            6. Avoid using `component` for individual classes with fields and methods. Instead, use `component` only for abstract modules or services without internal structure details. For concrete classes, always use `class` notation.
            7. Ensure all packages, classes, fields, and relationships are formatted correctly according to PlantUML standards, without any syntax errors.
            """.trimIndent()
        )

        promptBuilder.append("\nHere is the code structure data:\n")

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

        promptBuilder.append("\nOutput only the PlantUML component diagram code, without any additional text.")
        return promptBuilder.toString()
    }

    private fun StringBuilder.appendSection(title: String, items: List<String>) {
        if (items.isNotEmpty()) {
            append("$title:\n")
            items.forEach { item -> append("- $item\n") }
        }
    }

    /**
     * Helper function to append field details to the prompt.
     */
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

    /**
     * Helper function to append method details to the prompt.
     */
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
            append("    Details:\n")
            if (features.isNotEmpty()) append("    Features: ${features.joinToString(", ")}\n")
            if (mappings.isNotEmpty()) append("    Mappings: ${mappings.joinToString(", ")}\n")
            embeddings?.let { append("    Embedding: ${it.joinToString(", ")}\n") }
        }
    }
}