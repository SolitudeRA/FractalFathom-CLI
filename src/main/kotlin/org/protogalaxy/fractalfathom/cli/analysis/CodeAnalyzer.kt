package org.protogalaxy.fractalfathom.cli.analysis


import kotlinx.coroutines.*
import org.protogalaxy.fractalfathom.cli.analysis.ir.IRClassEntity
import org.protogalaxy.fractalfathom.cli.analysis.parsers.ClassParser
import spoon.Launcher
import spoon.reflect.CtModel
import spoon.reflect.declaration.CtClass
import java.io.File

/**
 * 主解析器类，负责解析单个 Java 类源代码并生成 IR 表示。
 */
class CodeAnalyzer(private val projectPath: String)  {

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

    private fun analyzeSingleFile(filePath: String): List<IRClassEntity> {
        val launcher = Launcher()
        launcher.addInputResource(filePath)

        // 设置 Spoon 环境配置
        launcher.environment.apply {
            noClasspath = true                // 忽略类路径问题
            ignoreSyntaxErrors = true         // 忽略语法错误
            isAutoImports = true              // 自动处理 import
        }

        try {
            launcher.buildModel()
        } catch (e: Exception) {
            println("Error building model for file: $filePath")
            e.printStackTrace()
            return emptyList()
        }

        val model: CtModel = launcher.model
        val classes = model.allTypes.filterIsInstance<CtClass<*>>()
        val classParser = ClassParser()

        return classes.mapNotNull { ctClass ->
            try {
                classParser.parseClass(ctClass, filePath)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

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