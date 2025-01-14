package com.yadnyeshkolte.crossdocs.parser

data class TaskItem(
    val text: String,
    val isChecked: Boolean
)

object TaskListParser {
    fun parseTaskList(text: String): List<TaskItem>? {
        // Only parse if it starts with "Task List" marker
        if (!text.trimStart().startsWith("Task List")) {
            return null
        }

        return text
            .substringAfter("Task List")  // Get content after "Task List" marker
            .trim()
            .split("\n")
            .filter { it.trim().startsWith("- [") }
            .map { line ->
                val isChecked = line.contains("- [x]", ignoreCase = true)
                val text = line.substringAfter("]").trim()
                TaskItem(text, isChecked)
            }
    }

    fun isTaskList(text: String): Boolean {
        return text.trimStart().startsWith("Task List")
    }
}