package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.parser.Footnote

@Composable
fun FootnoteComponent(
    footnote: Footnote,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(
                    baselineShift = BaselineShift.Superscript,
                    fontSize = 12.sp
                )) {
                    append("[^${footnote.id}]")
                }
            },
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = footnote.content)
    }
}