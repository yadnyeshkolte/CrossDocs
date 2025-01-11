package com.yadnyeshkolte.crossdocs.parser

data class TaskListItem(
    val text: String,
    val isChecked: Boolean
)

object TaskListParser {
    private val TASK_ITEM_REGEX = """- \[([ xX])\] (.+)""".toRegex()

    fun parseTaskList(text: String): List<TaskListItem>? {
        val lines = text.trim().lines()
        if (!lines.any { it.matches(TASK_ITEM_REGEX) }) return null

        return lines
            .filter { it.matches(TASK_ITEM_REGEX) }
            .map { line ->
                val match = TASK_ITEM_REGEX.find(line)!!
                val isChecked = match.groupValues[1].lowercase() == "x"
                val taskText = match.groupValues[2].trim()
                TaskListItem(taskText, isChecked)
            }
    }
}