package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeBlock(
    code: String,
    language: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Language label if provided
        if (!language.isNullOrBlank()) {
            Text(
                text = language,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Code content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = code,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF24292E)
                )
            )
        }
    }
}