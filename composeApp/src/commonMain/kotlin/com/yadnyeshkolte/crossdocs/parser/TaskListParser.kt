package com.yadnyeshkolte.crossdocs.parser

import com.yadnyeshkolte.crossdocs.model.TaskListItem
fun parseTaskList(text: String): List<TaskListItem> {
    val pattern = """- \[([ xX])\] (.+)""".toRegex()
    return text.lines()
        .filter { it.matches(pattern) }
        .map { line ->
            val match = pattern.find(line)!!
            val isCompleted = match.groupValues[1].lowercase() == "x"
            val taskText = match.groupValues[2]
            TaskListItem(taskText, isCompleted)
        }
}


