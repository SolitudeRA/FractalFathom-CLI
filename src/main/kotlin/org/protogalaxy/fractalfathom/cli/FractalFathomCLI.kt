package org.protogalaxy.fractalfathom.cli

import kotlinx.coroutines.runBlocking
import org.protogalaxy.fractalfathom.cli.analysis.CodeAnalyzer
import org.protogalaxy.fractalfathom.cli.modelInference.GraphCodeBERTUtils
import org.protogalaxy.fractalfathom.cli.modelInference.LLMUtils
import org.protogalaxy.fractalfathom.cli.visualization.DiagramViewer
import java.io.File

class FractalFathomCLI(private val projectPath: String) {

    fun run() = runBlocking {

        // 创建 CodeAnalyzer 实例
        val codeAnalyzer = CodeAnalyzer(projectPath)
        val irData = codeAnalyzer.analyzeProject()

        // 创建 GraphCodeBERTUtils 实例，生成增强的IR数据
        val graphCodeBERTUtils = GraphCodeBERTUtils()
        val enhancedIrData = graphCodeBERTUtils.enhanceIRDataWithEmbeddings(irData)

        // 使用 LLMUtils 调用 GPT-4 生成 PlantUML 组件图
        val llmUtils = LLMUtils()
        val plantUMLCode = llmUtils.generatePlantUML(enhancedIrData)

        // 将 PlantUML 代码保存到文件
        val plantUMLPath = "component_diagram.puml"
        savePlantUMLToFile(plantUMLCode, plantUMLPath)
        println("组件图的 PlantUML 代码已保存到 $plantUMLPath")

        // 生成组件图图片
        val diagramViewer = DiagramViewer()
        val outputImagePath = "component_diagram.png"
        diagramViewer.generateDiagramImage(plantUMLCode, outputImagePath)
        println("组件图已生成并保存到 $outputImagePath")
    }

    private fun savePlantUMLToFile(plantUMLCode: String, outputPath: String) {
        val file = File(outputPath)
        file.writeText(plantUMLCode)
    }
}