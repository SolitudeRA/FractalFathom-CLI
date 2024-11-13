package org.protogalaxy.fractalfathom.cli

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("请提供要分析的项目路径")
        return
    }
    val projectPath = args[0]
    val cli = FractalFathomCLI(projectPath)
    cli.run()
}