package org.protogalaxy.fractalfathom.cli

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide the path to the project to be analyzed")
        return
    }
    val projectPath = args[0]
    val outputDir = args[1]
    val analyzeOnly = args.contains("--analyze-only")
    val cli = FractalFathomCLI(projectPath, outputDir, analyzeOnly)
    cli.run()
}