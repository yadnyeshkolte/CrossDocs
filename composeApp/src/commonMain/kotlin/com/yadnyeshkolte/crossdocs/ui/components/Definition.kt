package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yadnyeshkolte.crossdocs.parser.DefinitionItem

@Composable
fun Definition(
    item: DefinitionItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = item.term,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.definition,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}