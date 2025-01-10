package com.yadnyeshkolte.crossdocs.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
)

@Composable
fun ChatSection(
    modifier: Modifier = Modifier,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    var currentPrompt by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        // Chat Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message)
            }
        }

        // Auto-scroll to bottom when new message is added
        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        // Input Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentPrompt,
                onValueChange = { currentPrompt = it },
                placeholder = { Text("Ask Gemini anything...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF7F52FF),
                    unfocusedBorderColor = Color.Gray
                )
            )

            IconButton(
                onClick = {
                    if (currentPrompt.isNotEmpty()) {
                        onSendMessage(currentPrompt)
                        currentPrompt = ""
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                    }
                }
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color(0xFF7F52FF)
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val clipboardManager = LocalClipboardManager.current
    var showCopyConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (message.isUser) Color(0xFF7F52FF) else Color.White,
                    RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    if (message.isUser) Color.Transparent else Color.LightGray,
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            // Make text selectable for non-user messages
            if (!message.isUser) {
                SelectionContainer {
                    Text(
                        text = message.content,
                        color = Color.Black
                    )
                }
                // Add copy button for non-user messages
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(message.content))
                        showCopyConfirmation = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF7F52FF)
                    )
                ) {
                    Text("Copy")
                }
            } else {
                Text(
                    text = message.content,
                    color = Color.White
                )
            }
        }

        if (showCopyConfirmation) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    TextButton(onClick = { showCopyConfirmation = false }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text("Text copied to clipboard")
            }
            LaunchedEffect(showCopyConfirmation) {
                kotlinx.coroutines.delay(2000)
                showCopyConfirmation = false
            }
        }

        Text(
            text = if (message.isUser) "You" else "Gemini",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}