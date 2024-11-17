package org.protogalaxy.fractalfathom.cli.analysis


import kotlinx.coroutines.*
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.parsers.ClassParser
import spoon.Launcher
import spoon.reflect.CtModel
import spoon.reflect.declaration.CtClass
import java.io.File

/**
 * A class responsible for analyzing Java source code files in a given project directory.
 *
 * This class uses Spoon, a library for Java source code analysis and transformation,
 * to parse Java files and extract intermediate representation (IR) for classes.
 *
 * @param projectPath The root directory of the project containing Java source files.
 */
class CodeAnalyzer(private val projectPath: String)  {

    /**
     * Analyzes all Java files in the project directory and returns a list of IRClassEntity objects.
     *
     * @return A list of IRClassEntity objects representing the analyzed classes in the project.
     */
    suspend fun analyzeProject(): List<IRClassEntity> = withContext(Dispatchers.Default) {
        val javaFilePaths = getAllJavaFilePaths()
        val deferredList = javaFilePaths.map { filePath ->
            async {
                try {
                    analyzeSingleFile(filePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList<IRClassEntity>()
                }
            }
        }
        deferredList.awaitAll().flatten()
    }

    /**
     * Analyzes a single Java file and extracts IRClassEntity objects from it.
     *
     * @param filePath The absolute path to the Java file to analyze.
     * @return A list of IRClassEntity objects extracted from the Java file.
     */
    private fun analyzeSingleFile(filePath: String): List<IRClassEntity> {
        val launcher = Launcher()
        launcher.addInputResource(filePath)

        launcher.environment.apply {
            noClasspath = true          // Allow analysis without resolving external dependencies
            ignoreSyntaxErrors = true   // Ignore syntax errors in the code
            isAutoImports = true        // Enable automatic import handling
        }

        try {
            launcher.buildModel()
        } catch (e: Exception) {
            println("Error building model for file: $filePath")
            e.printStackTrace()
            return emptyList() // Return an empty list if model building fails
        }

        // Extract all classes from the Spoon model
        val model: CtModel = launcher.model
        val classes = model.allTypes.filterIsInstance<CtClass<*>>()
        val classParser = ClassParser()

        // Parse each class and convert it to IRClassEntity
        return classes.mapNotNull { ctClass ->
            try {
                classParser.parseClass(ctClass, filePath)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Recursively collects all Java file paths from the project directory.
     *
     * @return A list of absolute paths to all Java files in the project directory.
     */
    private fun getAllJavaFilePaths(): List<String> {
        val javaFiles = mutableListOf<String>()
        File(projectPath).walkTopDown().forEach { file ->
            if (file.isFile && file.extension == "java") {
                javaFiles.add(file.absolutePath)
            }
        }
        return javaFiles
    }
}