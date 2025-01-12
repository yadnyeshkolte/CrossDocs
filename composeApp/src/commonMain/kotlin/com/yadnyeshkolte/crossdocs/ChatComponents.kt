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
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(message)
            }
        }

        LaunchedEffect(messages.size) {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentPrompt,
                onValueChange = { currentPrompt = it },
                placeholder = {
                    Text(
                        "Ask Gemini anything...",
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                    textColor = MaterialTheme.colors.onSurface,
                    cursorColor = MaterialTheme.colors.primary
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
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}

// ChatMessageItem modifications
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
                    if (message.isUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
                    RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    if (message.isUser) Color.Transparent else MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            if (!message.isUser) {
                SelectionContainer {
                    Text(
                        text = message.content,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                TextButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(message.content))
                        showCopyConfirmation = true
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text("Copy")
                }
            } else {
                Text(
                    text = message.content,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }

        Text(
            text = if (message.isUser) "You" else "Gemini",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}