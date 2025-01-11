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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.withStyle
import com.yadnyeshkolte.crossdocs.parser.DefinitionItem
import com.yadnyeshkolte.crossdocs.parser.DefinitionParser
import com.yadnyeshkolte.crossdocs.parser.EmojiParser
import com.yadnyeshkolte.crossdocs.parser.TaskListParser
import com.yadnyeshkolte.crossdocs.ui.components.CodeBlock
import com.yadnyeshkolte.crossdocs.ui.components.Definition
import com.yadnyeshkolte.crossdocs.ui.components.RichTextWithEmoji
import com.yadnyeshkolte.crossdocs.ui.components.TaskList
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes

@Composable
fun App() {
    MaterialTheme {
        var markdownText by remember { mutableStateOf("") }
        var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
        val geminiService = remember { GeminiService() }
        val scope = rememberCoroutineScope()

        val uriHandler = LocalUriHandler.current

        DisposableEffect(Unit) {
            onDispose {
                geminiService.dispose()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("CrossDocs", color = Color.White) },
                backgroundColor = Color(0xFF7F52FF),
                actions = {
                    DropdownMenuButton("Home", listOf("New", "Open", "Save", "Close"))
                    DropdownMenuButton("MGuide", listOf("Edit Option 1", "Edit Option 2"))
                    TextButton(onClick = {
                        uriHandler.openUri("https://github.com/yadnyeshkolte/CrossDocs")
                    }) {
                        Text("Help", color = Color.White)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Left Side: Markdown editor and chat section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    // Markdown Editor with red border
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Markdown takes up available space until Chat
                            .padding(8.dp) // Padding inside the border
                    ) {
                        Text("Write Markdown Here:")
                        BasicTextField(
                            value = markdownText,
                            onValueChange = { markdownText = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.LightGray.copy(alpha = 0.2f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        )
                    }

                    // Chat Section with purple border
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Chat takes the remaining space
                            .padding(top = 8.dp)
                            .border(
                                width = 2.dp,
                                color = Color(0xFF7F52FF), // Purple border
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(8.dp) // Padding inside the border
                    ) {
                        ChatSection(
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

                // Right Side: Live Preview
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
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


sealed class TextSegment {
    data class Regular(val content: String) : TextSegment()
    data class Table(val content: String) : TextSegment()
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
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )

    when (node.type) {
        MarkdownElementTypes.PARAGRAPH -> {
            val text = node.getTextInNode(originalText).toString()

            // Check if this is an image

            // Parse definitions if present
            val definitions = DefinitionParser.parseDefinitions(text)
            if (definitions.isNotEmpty()) {
                definitions.forEach { definition ->
                    Definition(
                        item = DefinitionItem(
                            term = EmojiParser.parseEmojis(definition.term),
                            definition = EmojiParser.parseEmojis(definition.definition)
                        )
                    )
                }
            } else {
                // Split the text into segments (table and non-table parts)
                val segments = splitTextAndTables(text)

                segments.forEach { segment ->
                    when (segment) {
                        is TextSegment.Regular -> {
                            if (segment.content.isNotEmpty()) {
                                // Process emojis and formatting only once
                                val processedText = EmojiParser.parseEmojis(segment.content)

                                Text(
                                    text = buildAnnotatedString {
                                        processTextWithFormatting(processedText, this)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        is TextSegment.Table -> {
                            val tableParser = MarkdownTableParser()
                            tableParser.parseTable(segment.content)?.let { tableData ->
                                MarkdownTable(tableData)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
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
            RichTextWithEmoji(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 26.sp,  // Largest header
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        MarkdownElementTypes.ATX_2 -> {  // ## Header 2
            RichTextWithEmoji(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        MarkdownElementTypes.ATX_3 -> {  // ### Header 3
            RichTextWithEmoji(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        MarkdownElementTypes.ATX_4 -> {  // #### Header 4
            RichTextWithEmoji(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 20.sp,  // Adjusted to make it smaller than normal text
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        MarkdownElementTypes.ATX_5 -> {  // ##### Header 5
            RichTextWithEmoji(
                text = node.getTextInNode(originalText).toString().trimStart('#').trim(),
                style = TextStyle(
                    fontSize = 18.sp,  // Adjusted to make it smaller than normal text
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        MarkdownElementTypes.ATX_6 -> {  // ###### Header 6
            RichTextWithEmoji(
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


        MarkdownElementTypes.PARAGRAPH -> {
            val text = node.getTextInNode(originalText).toString()

            // Split text by newlines to identify potential table sections
            val lines = text.split("\n")
            var currentText = StringBuilder()

            var i = 0
            while (i < lines.size) {
                val line = lines[i].trim()

                // Check for table pattern (at least 2 lines starting with |)
                if (line.startsWith("|") &&
                    i + 1 < lines.size &&
                    lines[i + 1].trim().startsWith("|")) {

                    // Flush any accumulated text before the table
                    if (currentText.isNotEmpty()) {
                        Text(
                            text = processTextFormatting(currentText.toString().trim()),
                            modifier = Modifier.fillMaxWidth()
                        )
                        currentText.clear()
                    }

                    // Collect table lines
                    val tableLines = mutableListOf<String>()
                    while (i < lines.size && lines[i].trim().startsWith("|")) {
                        tableLines.add(lines[i].trim())
                        i++
                    }
                    i-- // Adjust index since we'll increment in the loop

                    // Parse and render table
                    val tableParser = MarkdownTableParser()
                    tableParser.parseTable(tableLines.joinToString("\n"))?.let { tableData ->
                        MarkdownTable(tableData)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                } else {
                    currentText.append(line).append("\n")
                }
                i++
            }

            // Flush any remaining text
            if (currentText.isNotEmpty()) {
                Text(
                    text = processTextFormatting(currentText.toString().trim()),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }


        /*else -> {
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
        }*/
        else -> {
            node.children.forEach { child ->
                renderNode(child, originalText)
            }
        }
    }
}

private fun processTextWithFormatting(text: String, spanBuilder: AnnotatedString.Builder) {
    var currentText = text

    // Define formatting rules
    data class FormattingRule(
        val regex: Regex,
        val style: (String) -> SpanStyle
    )

    val formattingRules = listOf(
        FormattingRule(
            "\\*\\*(.*?)\\*\\*".toRegex(),
            { SpanStyle(fontWeight = FontWeight.Bold) }
        ),
        FormattingRule(
            "\\*(.*?)\\*".toRegex(),
            { SpanStyle(fontStyle = FontStyle.Italic) }
        ),
        FormattingRule(
            "`(.*?)`".toRegex(),
            { SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = Color.Gray.copy(alpha = 0.3f)
            ) }
        ),
        FormattingRule(
            "~~(.*?)~~".toRegex(),
            { SpanStyle(textDecoration = TextDecoration.LineThrough) }
        ),
        FormattingRule(
            "~(.*?)~".toRegex(),
            { SpanStyle(
                baselineShift = BaselineShift.Subscript,
                fontSize = (14 * 0.75).sp
            ) }
        ),
        FormattingRule(
            "\\^(.*?)\\^".toRegex(),
            { SpanStyle(
                baselineShift = BaselineShift.Superscript,
                fontSize = (14 * 0.75).sp
            ) }
        ),
        FormattingRule(
            "==(.*?)==".toRegex(),
            { SpanStyle(background = Color(0xFF7F52FF).copy(alpha = 0.3f)) }
        ),
        FormattingRule(
            "\\[(.*?)\\]\\((.*?)\\)".toRegex(),  // Matches [text](url) pattern
            { SpanStyle(color = Color(0xFF7F52FF)) }    // Makes the text yellow
        )

    )

    while (currentText.isNotEmpty()) {
        // Find all matches for all rules
        val allMatches = formattingRules.flatMap { rule ->
            rule.regex.findAll(currentText).map { match ->
                Triple(match, match.range.first, rule)
            }
        }

        // If no matches found, append remaining text and break
        if (allMatches.isEmpty()) {
            spanBuilder.append(currentText)
            break
        }

        // Get the first match based on position
        val (firstMatch, startIndex, matchedRule) = allMatches.minByOrNull { it.second }!!

        // Append text before the match
        if (startIndex > 0) {
            spanBuilder.append(currentText.substring(0, startIndex))
        }

        // Apply the style to the matched content
        spanBuilder.withStyle(matchedRule.style(firstMatch.groupValues[1])) {
            append(firstMatch.groupValues[1])
        }

        // Move to the remaining text
        currentText = currentText.substring(firstMatch.range.last + 1)
    }
}

private fun splitTextAndTables(text: String): List<TextSegment> {
    val segments = mutableListOf<TextSegment>()
    val lines = text.split("\n")
    var currentText = StringBuilder()
    var tableLines = mutableListOf<String>()
    var inTable = false

    fun flushCurrentText() {
        if (currentText.isNotEmpty()) {
            segments.add(TextSegment.Regular(currentText.toString().trim()))
            currentText.clear()
        }
    }

    fun flushTableLines() {
        if (tableLines.isNotEmpty()) {
            segments.add(TextSegment.Table(tableLines.joinToString("\n")))
            tableLines.clear()
        }
    }

    lines.forEach { line ->
        val trimmedLine = line.trim()

        when {
            trimmedLine.startsWith("|") && trimmedLine.endsWith("|") -> {
                if (!inTable) {
                    flushCurrentText()
                    inTable = true
                }
                tableLines.add(trimmedLine)
            }
            inTable && trimmedLine.contains("|") -> {
                tableLines.add(trimmedLine)
                if (!trimmedLine.endsWith("|")) {
                    flushTableLines()
                    inTable = false
                }
            }
            else -> {
                if (inTable) {
                    flushTableLines()
                    inTable = false
                }
                if (trimmedLine.isNotEmpty()) {
                    currentText.append(trimmedLine).append("\n")
                }
            }
        }
    }

    flushCurrentText()
    if (inTable) {
        flushTableLines()
    }

    return segments
}

private fun processRemainingFormatting(text: String, spanBuilder: AnnotatedString.Builder) {
    var currentText = text

    // Process Strikethrough (~~text~~)
    val strikethroughRegex = "~~(.*?)~~".toRegex()
    currentText = processFormatting(currentText, strikethroughRegex, spanBuilder) { content ->
        SpanStyle(textDecoration = TextDecoration.LineThrough)
    }

    // Process Subscript (~text~)
    val subscriptRegex = "~(.*?)~".toRegex()
    currentText = processFormatting(currentText, subscriptRegex, spanBuilder) { content ->
        SpanStyle(
            baselineShift = BaselineShift.Subscript,
            fontSize = (14 * 0.75).sp
        )
    }

    // Process Superscript (^text^)
    val superscriptRegex = "\\^(.*?)\\^".toRegex()
    currentText = processFormatting(currentText, superscriptRegex, spanBuilder) { content ->
        SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = (14 * 0.75).sp
        )
    }

    // Process Highlight (==text==)
    val highlightRegex = "==(.*?)==".toRegex()
    currentText = processFormatting(currentText, highlightRegex, spanBuilder) { content ->
        SpanStyle(background = Color(0xFF7F52FF).copy(alpha = 0.3f))
    }

    if (currentText.isNotEmpty()) {
        spanBuilder.append(currentText)
    }
}

private fun processFormatting(
    text: String,
    regex: Regex,
    spanBuilder: AnnotatedString.Builder,
    styleProvider: (String) -> SpanStyle
): String {
    var currentText = text
    var lastIndex = 0

    regex.findAll(currentText).forEach { result ->
        val startIndex = result.range.first
        val endIndex = result.range.last + 1

        if (startIndex > lastIndex) {
            spanBuilder.append(currentText.substring(lastIndex, startIndex))
        }

        spanBuilder.withStyle(styleProvider(result.groupValues[1])) {
            append(result.groupValues[1])
        }

        lastIndex = endIndex
    }

    return if (lastIndex < currentText.length) {
        currentText.substring(lastIndex)
    } else {
        ""
    }
}