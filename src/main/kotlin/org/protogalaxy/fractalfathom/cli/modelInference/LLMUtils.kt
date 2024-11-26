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
import java.io.File
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
        val promptFile = File("C:/Users/Solitude/Desktop/prompt_output.txt")
        promptFile.writeText(prompt)
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
        promptBuilder.append("""
            Using the following syntax and code structure data, generate a PlantUML class diagram organized by software functionality. 
            Represent each functionality as a package in the diagram, and group relevant classes within the corresponding package. 
            Utilize the provided data structure to ensure accurate representation of features, mappings, and relationships. Follow these steps:
        """.trimIndent())
        appendRequirements(promptBuilder)
        promptBuilder.append("The syntax of the PlantUML class diagram are as follow:\n")
        appendSyntaxDescription(promptBuilder)
        promptBuilder.append("\nHere is the code structure data:\n")
        irClasses.forEach { irClass -> appendClassData(promptBuilder, irClass) }
        appendAdditionalRequirements(promptBuilder)

        return promptBuilder.toString()
    }

    private fun appendRequirements(promptBuilder: StringBuilder) {
        promptBuilder.append(
            """
                
            ---
            ### **1. Parse the Input Data**
            - **Classes**: Extract information about each class, including its name, type, fields, methods, visibility modifiers, and annotations.
              - `Embeddings`: Although embeddings are provided, they are not required in the visual representation but should be retained in comments for traceability.
              - `Features`: Each class may include feature information (e.g., name, type, description). Use this data to organize classes into functional packages.
              - `Mappings`: Classes, fields, and methods may have mappings to higher-order concepts (e.g., COMPONENT, CONCEPT, DATA). Use these mappings to label or categorize elements in the diagram.
            - **Relationships**: Identify relationships between classes (inheritance, association, dependency, etc.), their direction, and multiplicity, if available.
            - **Fields and Methods**: Include fields and methods for each class, along with their visibility modifiers (`+`, `-`, `#`, `~`).

            ### **2. Organize Classes by Functionality**
            - Group classes into PlantUML packages based on their `Features` and `Mappings`.
              - Use `Features` to identify functional groupings (e.g., "User Management Module").
              - Use `Mappings` to assign classes or elements to specific concepts (e.g., `COMPONENT`, `MODULE`, `DATA`).

            ### **3. Generate the PlantUML Script**
            - For each class:
              - Define its attributes (fields) and operations (methods) using their names, types, and visibility.
              - Annotate methods or fields with their associated mappings to concepts.
            - Represent relationships between classes:
              - Use appropriate PlantUML syntax (e.g., `--|>` for inheritance, `..|>` for interface realization, `o--` for aggregation, `*--` for composition).
              - Include multiplicities (`"1..*"`, `"0..1"`) if specified.
            - Group related classes into functional packages using the `package` keyword.
              - Example: `package "User Management Module" { ... }`.

            ### **4. Add Visual Enhancements**
            - Highlight key annotations or mappings (e.g., `@FractalFathomFeature` or `@FractalFathomMapping`) in the diagram.
            - Include comments in the PlantUML script to explain the representation of features, mappings, and relationships.
            - Add a legend to describe how annotations or mappings are represented.

            ### **5. Provide a Complete PlantUML Script**
            - Ensure the output is a valid and complete PlantUML script, ready for rendering without modification.
            - Retain embeddings in comments for traceability but do not use them in the visual diagram.
            - Ensure logical grouping and clarity in the diagram to facilitate understanding of the software's functional architecture.
            """.trimIndent()
        )
    }

    private fun appendSyntaxDescription(promptBuilder: StringBuilder) {
        promptBuilder.append("""
            
            ---
            1. **Basic Class Definition**:
               - Syntax for defining a class with attributes and methods.
               - Explain visibility modifiers (`+` for public, `-` for private, `#` for protected, `~` for package).
               - Include examples of defining attributes (`attributeName : Type`) and methods (`methodName(param: Type): ReturnType`).

            2. **Interface Definition**:
               - Syntax for defining an interface using `interface InterfaceName`.
               - Explain how interfaces differ from classes in PlantUML.

            3. **Enum Definition**:
               - Syntax for creating an enumeration using `enum EnumName`.
               - Provide an example with multiple values listed inside the enum.

            4. **Relationships Between Classes**:
               - Explain and describe the syntax for different relationships with examples:
                 - **Generalization (Inheritance)**: `ClassChild --|> ClassParent`
                 - **Realization (Interface Implementation)**: `ClassChild ..|> InterfaceName`
                 - **Association**: `ClassA --> ClassB`
                 - **Dependency**: `ClassA ..> ClassB`
                 - **Aggregation**: `ClassA o-- ClassB`
                 - **Composition**: `ClassA *-- ClassB`
               - Show how to add navigation direction (`-->`, `<--`) and specify multiplicity (`"1"`, `"many"`).

            5. **Class Modifiers**:
               - Syntax for declaring abstract classes using `abstract class ClassName`.
               - Syntax for declaring static classes using `class ClassName <<static>>`.
               - Show how to add comments within a class definition.

            6. **Multiplicity in Relationships**:
               - Syntax for defining cardinality/multiplicity in relationships (e.g., `"1"` or `"1..*"`).

            7. **Packages**:
               - Syntax for organizing classes into packages using `package PackageName { ... }`.
               - Provide an example of grouping multiple classes inside a package.

            8. **Generics**:
               - Syntax for defining generic classes using `class ClassName<T>`.
               - Provide an example of a method using a generic parameter.

            9. **Method Annotations**:
               - Syntax for adding annotations to methods using `methodName() : ReturnType <<annotation>>`.

            10. **Colors and Styles**:
                - Syntax for customizing class appearance with colors using `class ClassName #color`.
                - Include an example to change the background color of a class.

            11. **Aliases**:
                - Syntax for assigning aliases to classes using `class "Long Class Name" as AliasName`.
                - Show how aliases can simplify relationships in large diagrams.

            12. **Notes and Explanations**:
                - Syntax for adding standalone notes using `note "This is a note" as NoteID`.
                - Syntax for attaching notes to classes or relationships.

            13. **Controlling Visibility**:
                - Commands to hide or show specific attributes or methods (`hide ClassName methods`, `show ClassName attributes`).

            14. **Legends**:
                - Syntax for adding legends to diagrams using `legend ... endlegend`.
                - Include an example of a legend explaining the diagram's components.

            15. **Complete Example**:
                - Provide a full example diagram that incorporates all the above features.
                - Include comments explaining how each part of the diagram is implemented.

            Ensure that each feature includes:
            - The precise PlantUML syntax required.
            - A detailed explanation of what the syntax achieves.
            - Clear and practical examples for real-world use.
            
        """.trimIndent())
    }

    private fun appendAdditionalRequirements(promptBuilder: StringBuilder){
        promptBuilder.append("""
            
            ---
            **Additional Requirements**:
            - Accurately represent all classes, features, and relationships from the input data.
            - Validate the PlantUML script to ensure it renders without errors.
            - Use comments to provide context for the design choices in the script.
        """.trimIndent())
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