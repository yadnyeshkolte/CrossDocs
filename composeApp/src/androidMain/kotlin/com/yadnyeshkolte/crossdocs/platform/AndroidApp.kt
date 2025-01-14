package com.yadnyeshkolte.crossdocs.platform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yadnyeshkolte.crossdocs.MarkdownRenderer
import com.yadnyeshkolte.crossdocs.chat.ChatMessage
import com.yadnyeshkolte.crossdocs.chat.ChatSection
import com.yadnyeshkolte.crossdocs.gemini.GeminiService
import com.yadnyeshkolte.crossdocs.ui.screens.mguide.MGuidePage
import kotlinx.coroutines.launch

enum class AndroidScreenType {
    MARKDOWN_EDITOR,
    PREVIEW,
    CHAT,
    MGUIDE
}


@Composable
fun AndroidWelcomeScreen(
    isDarkMode: Boolean,
    onNavigate: (AndroidScreenType) -> Unit
) {
    Surface(
        color = if (isDarkMode) Color(0xFF121212) else Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to CrossDocs",
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDarkMode) Color.White else Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            WelcomeButton(
                text = "Markdown Editor",
                onClick = { onNavigate(AndroidScreenType.MARKDOWN_EDITOR) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WelcomeButton(
                text = "Preview",
                onClick = { onNavigate(AndroidScreenType.PREVIEW) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WelcomeButton(
                text = "MGuide",
                onClick = { onNavigate(AndroidScreenType.MGUIDE) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WelcomeButton(
                text = "Gemini Chat",
                onClick = { onNavigate(AndroidScreenType.CHAT) }
            )
        }
    }
}
@Composable
private fun WelcomeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AndroidApp() {
    var currentScreen by remember { mutableStateOf<AndroidScreenType?>(null) }
    var markdownText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isDarkMode by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var menuAnchor by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }



    // Initialize GeminiService
    val geminiService = remember { GeminiService() }
    val scope = rememberCoroutineScope()

    // Dispose GeminiService when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            geminiService.dispose()
        }
    }


    val colors = if (isDarkMode) {
        darkColors(
            primary = Color(0xFF7F52FF),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFF7F52FF),
            background = Color.White,
            surface = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    MaterialTheme(colors = colors) {
        if (currentScreen == null) {
            AndroidWelcomeScreen(
                isDarkMode = isDarkMode,
                onNavigate = { screen ->
                    currentScreen = screen
                }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("CrossDocs", color = Color.White) },
                        backgroundColor = MaterialTheme.colors.primary,
                        navigationIcon = {
                            IconButton(onClick = { currentScreen = null }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            // Switch between Markdown and Preview button
                            if (currentScreen == AndroidScreenType.MARKDOWN_EDITOR || currentScreen == AndroidScreenType.PREVIEW) {
                                TextButton(
                                    onClick = {
                                        currentScreen = if (currentScreen == AndroidScreenType.MARKDOWN_EDITOR) {
                                            AndroidScreenType.PREVIEW
                                        } else {
                                            AndroidScreenType.MARKDOWN_EDITOR
                                        }
                                    }
                                ) {
                                    Text(
                                        text = if (currentScreen == AndroidScreenType.MARKDOWN_EDITOR) "Preview" else "Editor",
                                        color = Color.White
                                    )
                                }
                            }

                            // Dark mode switch with label
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Switch(
                                    checked = isDarkMode,
                                    onCheckedChange = { isDarkMode = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color.White.copy(alpha = 0.5f)
                                    )
                                )
                            }

                            // Menu button and dropdown
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.onGloballyPositioned { coordinates ->
                                    menuAnchor = coordinates
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                offset = DpOffset(0.dp, 0.dp)
                            ) {
                                DropdownMenuItem(onClick = {
                                    currentScreen = AndroidScreenType.MARKDOWN_EDITOR
                                    showMenu = false
                                }) {
                                    Text("Markdown Editor")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = AndroidScreenType.PREVIEW
                                    showMenu = false
                                }) {
                                    Text("Preview")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = AndroidScreenType.MGUIDE
                                    showMenu = false
                                }) {
                                    Text("MGuide")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = AndroidScreenType.CHAT
                                    showMenu = false
                                }) {
                                    Text("Gemini Chat")
                                }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (currentScreen) {
                        AndroidScreenType.MARKDOWN_EDITOR -> {
                            AndroidMarkdownEditor(
                                markdownText = markdownText,
                                onMarkdownTextChange = { markdownText = it }
                            )
                        }
                        AndroidScreenType.PREVIEW -> {
                            AndroidPreview(markdownText = markdownText)
                        }
                        AndroidScreenType.CHAT -> {
                            AndroidChatSection(
                                messages = messages,
                                onSendMessage = { prompt ->
                                    scope.launch {
                                        messages = messages + ChatMessage(
                                            content = prompt,
                                            isUser = true
                                        )
                                        val response = geminiService.generateResponse(prompt)
                                        messages = messages + ChatMessage(
                                            content = response,
                                            isUser = false
                                        )
                                    }
                                }
                            )
                        }
                        AndroidScreenType.MGUIDE -> {
                            MGuidePage()
                        }
                        null -> {} // Should never happen in this branch
                    }
                }
            }
        }
    }
}

@Composable
fun AndroidMarkdownEditor(
    markdownText: String,
    onMarkdownTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Write Markdown Here:",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        BasicTextField(
            value = markdownText,
            onValueChange = onMarkdownTextChange,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colors.primary), // Add this line for cursor color
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}


@Composable
fun AndroidPreview(markdownText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Preview:",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            MarkdownRenderer(
                markdownText = markdownText,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun AndroidChatSection(
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ChatSection(
            messages = messages,
            onSendMessage = onSendMessage
        )
    }
}