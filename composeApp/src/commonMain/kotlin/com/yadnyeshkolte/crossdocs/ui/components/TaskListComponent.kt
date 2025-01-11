package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.parser.TaskListItem

@Composable
fun TaskList(items: List<TaskListItem>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        items.forEach { item ->
            var checked by remember { mutableStateOf(item.isChecked) }
            TaskRow(
                text = item.text,
                isChecked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}

@Composable
private fun TaskRow(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF7F52FF),
                uncheckedColor = Color(0xFF7F52FF).copy(alpha = 0.6f),
                checkmarkColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                color = if (isChecked) Color.Gray else Color.Black,
                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
            )
        )
    }
}