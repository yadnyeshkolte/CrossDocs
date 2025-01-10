package com.yadnyeshkolte.crossdocs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.chat.ChatSection
import com.yadnyeshkolte.crossdocs.gemini.GeminiService
import kotlinx.coroutines.launch
import org.intellij.markdown.*
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import com.yadnyeshkolte.crossdocs.chat.ChatMessage
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import com.yadnyeshkolte.crossdocs.parser.MarkdownTableParser
import com.yadnyeshkolte.crossdocs.ui.components.MarkdownTable
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.parser.parseTaskList
import com.yadnyeshkolte.crossdocs.ui.components.TaskList


@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var markdownText by remember { mutableStateOf("") }
        var renderedHtml by remember { mutableStateOf("") }

        // Chat state
        var currentPrompt by remember { mutableStateOf("") }
        var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
        val geminiService = remember { GeminiService() }
        val scope = rememberCoroutineScope()


        val uriHandler = LocalUriHandler.current
        val normalTextStyle = TextStyle(
            fontSize = 14.sp,  // or any smaller size you prefer
            fontWeight = FontWeight.Normal
        )

        DisposableEffect(Unit) {
            onDispose {
                geminiService.dispose()
            }
        }


        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBar(
                title = { Text("CrossDocs", color = Color.White) },
                backgroundColor = Color(0xFF7F52FF),
                actions = {
                    DropdownMenuButton("File", listOf("New", "Open", "Save", "Close"))
                    DropdownMenuButton("Edit", listOf("Edit Option 1", "Edit Option 2"))
                    DropdownMenuButton("View", listOf("View Option 1", "View Option 2"))
                    TextButton(onClick = {
                        uriHandler.openUri("https://github.com/yadnyeshkolte/CrossDocs")
                    }) {
                        Text("Help", color = Color.White)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxSize()) {
                // Markdown Input
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text("Write Markdown Here:")
                    BasicTextField(
                        value = markdownText,
                        onValueChange = { markdownText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(
                                Color.LightGray.copy(alpha = 0.2f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp)
                    )
                }

                // Live Preview
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text("Preview:")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.White,
                                RoundedCornerShape(4.dp)
                            )
                            .border(
                                1.dp,
                                Color.LightGray,
                                RoundedCornerShape(4.dp)
                            )
                    ) {
                        MarkdownRenderer(
                            markdownText = markdownText,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }

                // Chat Section (60% of height)
                ChatSection(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth()
                        .padding(16.dp),
                    messages = messages,
                    onSendMessage = { prompt ->
                        scope.launch {
                            // Add user message
                            messages = messages + ChatMessage(
                                content = prompt,
                                isUser = true
                            )

                            // Get response from Gemini
                            val response = geminiService.generateResponse(prompt)
                            messages = messages + ChatMessage(
                                content = response,
                                isUser = false
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuButton(label: String, options: List<String>, onOptionClick: ((String) -> Unit)? = null) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(label, color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    onOptionClick?.invoke(option)
                }) {
                    Text(option)
                }
            }
        }
    }
}


@Composable
private fun processTextFormatting(text: String) = buildAnnotatedString {
    var currentIndex = 0
    // Handle strikethrough (~~text~~)
    val strikethroughRegex = "~~(.*?)~~".toRegex()
    strikethroughRegex.findAll(text).forEach { matchResult ->
        // Append any text before the match
        append(text.substring(currentIndex, matchResult.range.first))

        // Add the strikethrough text with style
        pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        append(matchResult.groupValues[1])
        pop()

        currentIndex = matchResult.range.last + 1
    }

    // Handle subscript (H~2~O)
    val subscriptRegex = "~(.*?)~".toRegex()
    subscriptRegex.findAll(text.substring(currentIndex)).forEach { matchResult ->
        // Append any text before the match
        append(text.substring(currentIndex, currentIndex + matchResult.range.first))

        // Add the subscript text with style
        pushStyle(SpanStyle(
            baselineShift = BaselineShift.Subscript,
            fontSize = (LocalTextStyle.current.fontSize.value * 0.75).sp
        ))
        append(matchResult.groupValues[1])
        pop()

        currentIndex += matchResult.range.last + 1
    }

    // Handle superscript (X^2^)
    val superscriptRegex = "\\^(.*?)\\^".toRegex()
    superscriptRegex.findAll(text.substring(currentIndex)).forEach { matchResult ->
        // Append any text before the match
        append(text.substring(currentIndex, currentIndex + matchResult.range.first))

        // Add the superscript text with style
        pushStyle(SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = (LocalTextStyle.current.fontSize.value * 0.75).sp
        ))
        append(matchResult.groupValues[1])
        pop()

        currentIndex += matchResult.range.last + 1
    }

    val highlightRegex = "==(.*?)==".toRegex()
    highlightRegex.findAll(text.substring(currentIndex)).forEach { matchResult ->
        append(text.substring(currentIndex, currentIndex + matchResult.range.first))
        pushStyle(SpanStyle(
            background = MaterialTheme.colors.primary.copy(alpha = 0.3f)  // Highlight color
        ))
        append(matchResult.groupValues[1])
        pop()
        currentIndex += matchResult.range.last + 1
    }

        // Append any remaining text
    if (currentIndex < text.length) {
        append(text.substring(currentIndex))
    }
}

@Composable
private fun TaskListItem(text: String, isChecked: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null, // Read-only checkbox
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF7F52FF),
                uncheckedColor = Color.Gray
            ),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
                color = if (isChecked) Color.Gray else Color.Black
            )
        )
    }
}


@Composable
fun MarkdownRenderer(markdownText: String, modifier: Modifier = Modifier) {
    val flavour = CommonMarkFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdownText)

    Column(modifier = modifier.padding(16.dp)) {
        renderNode(parsedTree, markdownText)
    }
}

@Composable
private fun renderNode(node: ASTNode, originalText: String, level: Int = 0) {
    val normalTextStyle = TextStyle(
        fontSize = 14.sp, // or any size you prefer
        fontWeight = FontWeight.Normal
    )

    when (node.type) {
        // PARAGRAPHS - Handles basic text with inline formatting
        MarkdownElementTypes.PARAGRAPH -> {

            val text = node.getTextInNode(originalText).toString()

            // Check if this contains task list items
            if (text.trim().startsWith("- [")) {
                val tasks = parseTaskList(text)
                if (tasks.isNotEmpty()) {
                    TaskList(tasks)
                    return
                }
            }

            Text(
                text = buildAnnotatedString {
                    node.children.forEach { child ->
                        when (child.type) {
                            // Italic text using *text* or _text_
                            MarkdownElementTypes.EMPH -> {
                                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                                append(child.getTextInNode(originalText).toString().trim('*', '_'))
                                pop()
                            }
                            MarkdownElementTypes.STRONG -> {
                                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                append(child.getTextInNode(originalText).toString()
                                    .trim('*', '_')
                                    .trimStart('*', '_')
                                    .trimEnd('*', '_'))
                                pop()
                            }
                            MarkdownElementTypes.CODE_SPAN -> {
                                pushStyle(SpanStyle(
                                    fontFamily = FontFamily.Monospace,
                                    background = Color.LightGray
                                ))
                                append(child.getTextInNode(originalText).toString().trim('`'))
                                pop()
                            }
                            else -> {
                                append(processTextFormatting(child.getTextInNode(originalText).toString()))
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        // LISTS - Handles both ordered (1. 2. 3.) and unordered (• • •) lists
        MarkdownElementTypes.UNORDERED_LIST -> {
            node.children.forEachIndexed { index, listItem ->  // Using `forEachIndexed` for ordered list numbering
                if (listItem.children.isNotEmpty()) { // Check if the listItem has children
                    Row(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = if (node.type == MarkdownElementTypes.ORDERED_LIST)
                                "${index+1}. "  // Automatically handled numbering
                            else "• "
                        )
                        Column {
                            listItem.children.forEach { renderNode(it, originalText) }
                        }
                    }
                }
            }
        }

        MarkdownElementTypes.ORDERED_LIST -> {
            var itemNumber = 1
            node.children.forEach { listItem ->
                val itemText = listItem.getTextInNode(originalText).toString().trim()
                if (itemText.isNotEmpty()) {
                    Row(modifier = Modifier.padding(start = 16.dp)) {
                        Text(text = "$itemNumber. ")
                        Column {
                            listItem.children.forEach { child ->
                                if (child.getTextInNode(originalText).toString().trim().isNotEmpty()) {
                                    renderNode(child, originalText)
                                }
                            }
                        }
                    }
                    itemNumber++
                }
            }
        }
        // HEADERS - Handles # and ## headers
        MarkdownElementTypes.ATX_1 -> {  // # Header 1
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 26.sp,  // Largest header
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        MarkdownElementTypes.ATX_2 -> {  // ## Header 2
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        MarkdownElementTypes.ATX_3 -> {  // ### Header 3
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        MarkdownElementTypes.ATX_4 -> {  // #### Header 4
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 20.sp,  // Adjusted to make it smaller than normal text
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        MarkdownElementTypes.ATX_5 -> {  // ##### Header 5
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 18.sp,  // Adjusted to make it smaller than normal text
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        MarkdownElementTypes.ATX_6 -> {  // ###### Header 6
            Text(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 16.sp,  // Significantly smaller than normal text
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        // Blockquote: > blockquote Work remaining about left side border
        MarkdownElementTypes.BLOCK_QUOTE -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(
                        width = 4.dp,
                        color = Color(0xFF7F52FF),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Column {
                    node.children.forEach { child ->
                        renderNode(child, originalText)
                    }
                }
            }
        }


        else -> {
            // Try to parse as table if the text starts with |
            val nodeText = node.getTextInNode(originalText).toString()
            if (nodeText.trimStart().startsWith("|")) {
                val tableParser = MarkdownTableParser()
                tableParser.parseTable(nodeText)?.let { tableData ->
                    MarkdownTable(tableData)
                    return
                }
            }
            else{
                node.children.forEach { child ->
                    renderNode(child, originalText)
                }
            }
        }
        /*else -> {
            node.children.forEach { child ->
                renderNode(child, originalText)
            }
        }*/
    }
}



