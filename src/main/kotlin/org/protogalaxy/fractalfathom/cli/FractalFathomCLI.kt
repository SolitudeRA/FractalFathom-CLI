package org.protogalaxy.fractalfathom.cli

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import org.protogalaxy.fractalfathom.cli.analysis.CodeAnalyzer
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils
import org.protogalaxy.fractalfathom.cli.visualization.DiagramViewer
import java.io.File

class FractalFathomCLI(private val projectPath: String, private val outputDir: String, private val analyzeOnly: Boolean = false) {

    private val mapper = jacksonObjectMapper()

    fun run() = runBlocking {

        val codeAnalyzer = CodeAnalyzer(projectPath)
        val irData = codeAnalyzer.analyzeProject()

        if (analyzeOnly) {
            val outputIrDataPath = "${outputDir}/FractalFathomOutput/irData.json"
            saveIrDataToFile(irData, outputIrDataPath)
            println("The IR data has been saved to $outputIrDataPath")
            return@runBlocking
        }

        val graphCodeBERTUtils = GraphCodeBERTUtils()
        var enhancedIrData = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(irData)

        val llmUtils = LLMUtils()
        val plantUMLCode = llmUtils.generatePlantUML(enhancedIrData)

        val outputPlantUMLPath = "${outputDir}/FractalFathomOutput/component_diagram.puml"
        savePlantUMLToFile(plantUMLCode, outputPlantUMLPath)
        println("The PlantUML code for the component diagram has been saved to $outputPlantUMLPath")

        val diagramViewer = DiagramViewer()
        val outputImagePath = "${outputDir}/FractalFathomOutput/component_diagram.png"
        diagramViewer.generateDiagramImage(plantUMLCode, outputImagePath)
        println("The component diagram has been generated and saved to $outputImagePath")
    }

    private fun saveIrDataToFile(irData: Any, outputPath: String) {
        val file = File(outputPath)
        file.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        file.writeText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(irData))
    }

    private fun savePlantUMLToFile(plantUMLCode: String, outputPath: String) {
        val file = File(outputPath)
        file.parentFile?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        file.writeText(plantUMLCode)
    }
}