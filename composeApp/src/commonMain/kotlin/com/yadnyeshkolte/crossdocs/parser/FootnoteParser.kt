package com.yadnyeshkolte.crossdocs.parser

data class Footnote(
    val id: String,
    val content: String
)

object FootnoteParser {
    private val footnoteRefRegex = "\\[\\^(.*?)]".toRegex()
    private val footnoteDefRegex = "\\[\\^(.*?)]:\\s*(.+)".toRegex()

    fun parseFootnotes(text: String): Pair<String, List<Footnote>> {
        val footnotes = mutableListOf<Footnote>()
        val lines = text.lines()
        var processedText = text

        // Find all footnote definitions
        lines.forEach { line ->
            footnoteDefRegex.find(line)?.let { match ->
                val id = match.groupValues[1]
                val content = match.groupValues[2].trim()
                footnotes.add(Footnote(id, content))
                // Remove the footnote definition from the text
                processedText = processedText.replace(line, "")
            }
        }

        return processedText to footnotes
    }

    fun hasFootnoteReference(text: String): Boolean {
        return footnoteRefRegex.containsMatchIn(text)
    }
}