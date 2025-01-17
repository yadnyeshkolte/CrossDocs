package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
fun MarkdownTable(
    tableData: TableData,
    textColor: Color = MaterialTheme.colors.onSurface,
    borderColor: Color = if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
    backgroundColor: Color = MaterialTheme.colors.surface
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        color = backgroundColor
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.1f))
            ) {
                tableData.headers.forEach { header ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        color = textColor,
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Rows
            tableData.rows.forEach { row ->
                Divider(color = borderColor)
                Row(modifier = Modifier.fillMaxWidth()) {
                    row.forEach { cell ->
                        Text(
                            text = cell,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            color = textColor
                        )
                    }
                }
            }
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