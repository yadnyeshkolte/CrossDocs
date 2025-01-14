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

// Using same enum as Android for consistency
enum class IOSScreenType {
    MARKDOWN_EDITOR,
    PREVIEW,
    CHAT,
    MGUIDE
}

@Composable
fun IOSWelcomeScreen(
    isDarkMode: Boolean,
    onNavigate: (IOSScreenType) -> Unit
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

            // iOS-styled buttons
            IOSWelcomeButton(
                text = "Markdown Editor",
                onClick = { onNavigate(IOSScreenType.MARKDOWN_EDITOR) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            IOSWelcomeButton(
                text = "Preview",
                onClick = { onNavigate(IOSScreenType.PREVIEW) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            IOSWelcomeButton(
                text = "MGuide",
                onClick = { onNavigate(IOSScreenType.MGUIDE) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            IOSWelcomeButton(
                text = "Gemini Chat",
                onClick = { onNavigate(IOSScreenType.CHAT) }
            )
        }
    }
}

@Composable
private fun IOSWelcomeButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), // Slightly shorter for iOS style
        shape = RoundedCornerShape(10.dp), // More iOS-like corner radius
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF007AFF), // iOS blue
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp, // iOS buttons typically don't have elevation
            pressedElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 17.sp, // iOS standard font size
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun IOSApp() {
    var currentScreen by remember { mutableStateOf<IOSScreenType?>(null) }
    var markdownText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var isDarkMode by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var menuAnchor by remember { mutableStateOf<androidx.compose.ui.layout.LayoutCoordinates?>(null) }

    // Initialize GeminiService
    val geminiService = remember { GeminiService() }
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            geminiService.dispose()
        }
    }

    // iOS-specific colors
    val colors = if (isDarkMode) {
        darkColors(
            primary = Color(0xFF007AFF), // iOS blue
            background = Color(0xFF000000),
            surface = Color(0xFF1C1C1E),
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColors(
            primary = Color(0xFF007AFF), // iOS blue
            background = Color(0xFFF2F2F7), // iOS light gray
            surface = Color.White,
            onBackground = Color.Black,
            onSurface = Color.Black
        )
    }

    MaterialTheme(colors = colors) {
        if (currentScreen == null) {
            IOSWelcomeScreen(
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
                            if (currentScreen == IOSScreenType.MARKDOWN_EDITOR || currentScreen == IOSScreenType.PREVIEW) {
                                TextButton(
                                    onClick = {
                                        currentScreen = if (currentScreen == IOSScreenType.MARKDOWN_EDITOR) {
                                            IOSScreenType.PREVIEW
                                        } else {
                                            IOSScreenType.MARKDOWN_EDITOR
                                        }
                                    }
                                ) {
                                    Text(
                                        text = if (currentScreen == IOSScreenType.MARKDOWN_EDITOR) "Preview" else "Editor",
                                        color = Color.White
                                    )
                                }
                            }

                            // Dark mode switch
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { isDarkMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = MaterialTheme.colors.primary
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            // Menu
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
                                    currentScreen = IOSScreenType.MARKDOWN_EDITOR
                                    showMenu = false
                                }) {
                                    Text("Markdown Editor")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = IOSScreenType.PREVIEW
                                    showMenu = false
                                }) {
                                    Text("Preview")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = IOSScreenType.MGUIDE
                                    showMenu = false
                                }) {
                                    Text("MGuide")
                                }
                                DropdownMenuItem(onClick = {
                                    currentScreen = IOSScreenType.CHAT
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
                        IOSScreenType.MARKDOWN_EDITOR -> {
                            IOSMarkdownEditor(
                                markdownText = markdownText,
                                onMarkdownTextChange = { markdownText = it }
                            )
                        }
                        IOSScreenType.PREVIEW -> {
                            IOSPreview(markdownText = markdownText)
                        }
                        IOSScreenType.CHAT -> {
                            IOSChatSection(
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
                        IOSScreenType.MGUIDE -> {
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
fun IOSMarkdownEditor(
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
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun IOSPreview(markdownText: String) {
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
fun IOSChatSection(
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