package com.yadnyeshkolte.crossdocs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import crossdocs.composeapp.generated.resources.compose_multiplatform
import crossdocs.composeapp.generated.resources.Res
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.launch



@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val uriHandler = LocalUriHandler.current



        var byteArray by remember { mutableStateOf(ByteArray(size = 0)) }



        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Title Bar at the Top
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
