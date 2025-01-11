package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.parser.TaskItem

@Composable
fun TaskListSection(tasks: List<TaskItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Title
        Text(
            text = "Task List",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Tasks
        tasks.forEach { task ->
            TaskListItem(task)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun TaskListItem(task: TaskItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Render the exact format: "- [x]" or "- [ ]"
        Text(
            text = "- [${if (task.isChecked) "x" else " "}] ",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
        )

        Text(
            text = task.text,
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}