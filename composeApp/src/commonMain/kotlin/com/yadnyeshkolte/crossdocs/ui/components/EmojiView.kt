package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmojiView(emoji: String, modifier: Modifier = Modifier) {
    Text(
        text = emoji,
        style = TextStyle(fontSize = 16.sp),
        modifier = modifier.padding(horizontal = 2.dp)
    )
}