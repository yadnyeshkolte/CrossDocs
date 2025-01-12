package com.yadnyeshkolte.crossdocs.parser

data class DefinitionItem(
    val term: String,
    val definition: String
)

object DefinitionParser {
    fun parseDefinitions(text: String): List<DefinitionItem> {
        val definitions = mutableListOf<DefinitionItem>()
        val lines = text.lines()

        var currentTerm: String? = null

        for (i in lines.indices) {
            val line = lines[i].trim()

            // Check for term (line not starting with ":")
            if (!line.startsWith(":") && line.isNotEmpty() &&
                i + 1 < lines.size && lines[i + 1].trimStart().startsWith(":")) {
                currentTerm = line
            }
            // Check for definition (line starting with ":")
            else if (line.startsWith(":") && currentTerm != null) {
                val definition = line.substring(1).trim()
                definitions.add(DefinitionItem(currentTerm, definition))
                currentTerm = null
            }
        }

        return definitions
    }
}
