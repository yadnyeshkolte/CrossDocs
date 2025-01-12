package com.yadnyeshkolte.crossdocs.model

import androidx.compose.ui.text.style.TextAlign

data class TableData(
    val headers: List<String>,
    val rows: List<List<String>>,
    val alignments: List<TextAlign>
)