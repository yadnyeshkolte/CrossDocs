package com.yadnyeshkolte.crossdocs

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CrossDocs",
    ) {
        App()
    }
}