package org.protogalaxy.fractalfathom.cli.modelInference

import kotlinx.coroutines.runBlocking
import okhttp3.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.protogalaxy.fractalfathom.cli.analysis.annotation.FeatureEntity
import org.protogalaxy.fractalfathom.cli.analysis.annotation.MappingEntity
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

    private val BASE_INDENT = 2

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
        appendRequirements(promptBuilder)

        promptBuilder.append("\nHere is the code structure data:\n")
        irClasses.forEach { irClass -> appendClassData(promptBuilder, irClass) }
        promptBuilder.append("\nOutput only the PlantUML component diagram code, without any additional text.")

        return promptBuilder.toString()
    }

    private fun appendRequirements(promptBuilder: StringBuilder) {
        promptBuilder.append(
            """
1. **Component Representation**:
   - Use `component` to represent each class, such as `UserService`, `RoleService`, `PermissionService`, `UserRepository`, `RoleRepository`, `PermissionRepository`, `User`.
   - Attach appropriate `<<stereotype>>` to each `component` based on the `Type` in `Features` or `Mappings` (e.g., `<<service>>`, `<<repository>>`, `<<entity>>`, `<<module>>`, `<<component>>`).

2. **Interface Representation**:
   - If applicable, use `interface` to represent interfaces or contracts.
   - Show implementation relationships using `..>` arrows with labels like `<<implements>>`.

3. **Relationships**:
   - Use arrows to represent dependencies and interactions between components:
     - `-->` for `uses` or dependency relationships.
     - `..>` for `implements` relationships.
   - Annotate arrows with method names or interactions specified in the `Methods` section (e.g., `createUser(username, email)`).

4. **Ports**:
   - Explicitly name ports or interaction points if mentioned in `Fields` or `Methods`.

5. **Grouping and Structure**:
   - Use keywords like `package`, `node`, `folder`, `frame`, `cloud`, `database` to group related components based on their domain (e.g., services, repositories, entities).
   - Use `database` to represent data repositories.

6. **Utilize Code Structure Data**:
   - Refer to the `Features`, `Mappings`, `Relations`, `Fields`, and `Methods` in the code structure data to inform component stereotypes and groupings.
   - Use the `Type` in `Features` to determine the `<<stereotype>>` of components.
   - Establish relationships and groupings between components based on the `To Concept` and `Type` in `Mappings`.
   - If there are `Mappings` in `Fields` and `Methods`, consider reflecting them in the component diagram.

7. **Ignore Non-relevant Data**:
   - Do not include `Embeddings`, code comments, or other irrelevant data in the diagram.

8. **Output Requirements**:
   - Provide only the PlantUML code without any additional descriptive text.
   - Ensure the code is syntactically correct and can be rendered without errors.
            """.trimIndent()
        )
    }

    private fun appendClassData(builder: StringBuilder, irClass: IRClassEntity) {
        builder.append("=== Class: ${irClass.name} (Type: ${irClass.type}) ===\n")
        irClass.embedding?.let { builder.append("Embeddings:\n  - ${it.values.joinToString(", ")}\n") }
        appendFeatures(builder, irClass.features, 0)
        appendMappings(builder, irClass.mappings, 0)
        appendRelations(builder, irClass.relations.map { "${it.relationType}: ${it.targetClass}" }, 0)
        appendFields(builder, irClass.fields, 0)
        appendMethods(builder, irClass.methods, 0)
    }

    private fun appendFeatures(builder: StringBuilder, features: List<FeatureEntity>, indentLevel: Int) {
        if (features.isNotEmpty()) {
            val currentIndent = " ".repeat(BASE_INDENT * indentLevel)
            builder.append("${currentIndent}Features (${features.size}):\n")
            features.forEach { feature ->
                builder.append("$currentIndent  - Name: ${feature.name}\n")
                builder.append("$currentIndent    Type: ${feature.type}\n")
                builder.append("$currentIndent    Description: ${feature.description}\n")
            }
        }
    }

    private fun appendMappings(builder: StringBuilder, mappings: List<MappingEntity>, indentLevel: Int) {
        if (mappings.isNotEmpty()) {
            val currentIndent = " ".repeat(BASE_INDENT * indentLevel)
            builder.append("${currentIndent}Mappings (${mappings.size}):\n")
            mappings.forEach { mapping ->
                builder.append("$currentIndent  - To Concept: ${mapping.toConcept}\n")
                builder.append("$currentIndent    Type: ${mapping.type}\n")
            }
        }
    }

    private fun appendRelations(builder: StringBuilder, relations: List<String>, indentLevel: Int) {
        if (relations.isNotEmpty()) {
            val currentIndent = " ".repeat(BASE_INDENT * indentLevel)
            builder.append("${currentIndent}Relations (${relations.size}):\n")
            relations.forEach { relation -> builder.append("$currentIndent- $relation\n") }
        }
    }

    /**
     * Helper function to append field details to the prompt.
     */
    private fun appendFields(builder: StringBuilder, fields: List<IRFieldEntity>?, indentLevel: Int) {
        fields?.takeIf { it.isNotEmpty() }?.let {
            val currentIndent = " ".repeat(BASE_INDENT * indentLevel)
            builder.append("Fields (${fields.size}):\n")
            fields.forEach { field ->
                builder.append("$currentIndent  - ${field.modifiers} ${field.type} ${field.name}\n")
                appendMappings(builder, field.mappings, indentLevel + BASE_INDENT)
            }
        }
    }

    /**
     * Helper function to append method details to the prompt.
     */
    private fun appendMethods(builder: StringBuilder, methods: List<IRMethodEntity>?, indentLevel: Int) {
        methods?.takeIf { it.isNotEmpty() }?.let {
            val currentIndent = " ".repeat(BASE_INDENT * indentLevel)
            builder.append("${currentIndent}Methods (${methods.size}):\n")
            methods.forEach { method ->
                builder.append(
                    "$currentIndent  - ${method.modifiers} ${method.returnType} ${method.name}(${
                        method.parameters.joinToString(
                            ", "
                        ) { "${it.type} ${it.name}" }
                    })\n"
                )
                appendMappings(builder, method.mappings, indentLevel + BASE_INDENT)
            }
        }
    }
}