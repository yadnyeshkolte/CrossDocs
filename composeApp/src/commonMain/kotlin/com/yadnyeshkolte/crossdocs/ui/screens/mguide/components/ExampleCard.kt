package com.yadnyeshkolte.crossdocs.ui.screens.mguide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yadnyeshkolte.crossdocs.ui.screens.mguide.model.MarkdownExample
import com.yadnyeshkolte.crossdocs.MarkdownRenderer

@Composable
fun ExampleCard(
    example: MarkdownExample,
    onCopyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                example.description,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Markdown Syntax:",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colors.onSurface
                )
                IconButton(onClick = onCopyClick) {
                    Text(
                        "Copy",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (MaterialTheme.colors.isLight) Color(0xFFF5F5F5) else Color(0xFF2A2A2A),
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        1.dp,
                        if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    example.markdownSyntax,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = MaterialTheme.colors.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Rendered Result:",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colors.surface,
                        RoundedCornerShape(4.dp)
                    )
                    .border(
                        1.dp,
                        if (MaterialTheme.colors.isLight) Color.LightGray else Color.DarkGray,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                MarkdownRenderer(
                    markdownText = example.markdownSyntax,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}