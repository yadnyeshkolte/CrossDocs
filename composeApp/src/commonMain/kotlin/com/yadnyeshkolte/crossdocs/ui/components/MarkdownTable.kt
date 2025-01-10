package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.model.TableData


@Composable
fun MarkdownTable(table: TableData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
    ) {
        // Header row with grey background
        TableRow(
            cells = table.headers,
            alignments = table.alignments,
            isHeader = true,
            backgroundColor = Color.LightGray.copy(alpha = 0.3f)
        )

        // Data rows
        table.rows.forEach { row ->
            TableRow(
                cells = row,
                alignments = table.alignments,
                isHeader = false,
                backgroundColor = Color.White
            )
        }
    }
}

@Composable
private fun TableRow(
    cells: List<String>,
    alignments: List<TextAlign>,
    isHeader: Boolean,
    backgroundColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .border(0.5.dp, Color.LightGray)
    ) {
        cells.forEachIndexed { index, cell ->
            val alignment = alignments.getOrNull(index) ?: TextAlign.Start

            // Add a box for each cell with border
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 40.dp) // Minimum height for rows
                    .then(
                        // Add right border for all cells except the last one
                        if (index < cells.size - 1) {
                            Modifier.border(
                                width = 0.5.dp,
                                color = Color.LightGray,
                                shape = androidx.compose.ui.graphics.RectangleShape
                            )
                        } else {
                            Modifier
                        }
                    )
                    .padding(8.dp),
                contentAlignment = when (alignment) {
                    TextAlign.Center -> Alignment.Center
                    TextAlign.End -> Alignment.CenterEnd
                    else -> Alignment.CenterStart
                }
            ) {
                Text(
                    text = cell,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
                    ),
                    textAlign = alignment
                )
            }
        }
    }
}