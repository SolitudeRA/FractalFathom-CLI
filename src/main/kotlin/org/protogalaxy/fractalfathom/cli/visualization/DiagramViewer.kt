package org.protogalaxy.fractalfathom.cli.visualization

import net.sourceforge.plantuml.SourceStringReader
import java.io.FileOutputStream

class DiagramViewer {

    fun generateDiagramImage(plantUMLCode: String, outputImagePath: String) {
        val reader = SourceStringReader(plantUMLCode)
        FileOutputStream(outputImagePath).use { fos ->
            reader.outputImage(fos)
        }
    }
}