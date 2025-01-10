package com.yadnyeshkolte.crossdocs.parser

import androidx.compose.ui.text.style.TextAlign
import com.yadnyeshkolte.crossdocs.model.TableData

class MarkdownTableParser {
    fun parseTable(markdownText: String): TableData? {
        val lines = markdownText.lines()
        if (lines.size < 3) return null // Need at least header, separator, and one data row

        // Find table start
        val tableStartIndex = lines.indexOfFirst { line ->
            line.trim().startsWith("|") && line.trim().endsWith("|")
        }
        if (tableStartIndex == -1) return null

        // Parse header row
        val headerRow = lines[tableStartIndex]
        val headers = parseTableRow(headerRow)

        // Parse separator row and alignments
        val separatorRow = lines[tableStartIndex + 1]
        val alignments = parseSeparatorRow(separatorRow)
        if (alignments.size != headers.size) return null

        // Parse data rows
        val dataRows = mutableListOf<List<String>>()
        var currentIndex = tableStartIndex + 2
        while (currentIndex < lines.size && lines[currentIndex].trim().startsWith("|")) {
            dataRows.add(parseTableRow(lines[currentIndex]))
            currentIndex++
        }

        return TableData(headers, dataRows, alignments)
    }

    private fun parseTableRow(row: String): List<String> {
        return row.trim()
            .removeSurrounding("|")
            .split("|")
            .map { it.trim() }
    }

    private fun parseSeparatorRow(separator: String): List<TextAlign> {
        return separator.trim()
            .removeSurrounding("|")
            .split("|")
            .map { cell ->
                val trimmed = cell.trim()
                when {
                    trimmed.startsWith(":") && trimmed.endsWith(":") -> TextAlign.Center
                    trimmed.endsWith(":") -> TextAlign.End
                    else -> TextAlign.Start
                }
            }
    }
}