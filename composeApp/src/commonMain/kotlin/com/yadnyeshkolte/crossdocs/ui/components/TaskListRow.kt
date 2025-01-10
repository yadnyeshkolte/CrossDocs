package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yadnyeshkolte.crossdocs.model.TaskListItem
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox



@Composable
fun TaskList(items: List<TaskListItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        items.forEach { item ->
            TaskListRow(item)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun TaskListRow(item: TaskListItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = null, // Read-only checkbox
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF7F52FF),
                uncheckedColor = Color.Gray.copy(alpha = 0.6f),
                checkmarkColor = Color.White
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = item.text,
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = if (item.isCompleted)
                    TextDecoration.LineThrough
                else
                    TextDecoration.None,
                color = if (item.isCompleted)
                    Color.Gray
                else
                    Color.Black
            )
        )
    }
}