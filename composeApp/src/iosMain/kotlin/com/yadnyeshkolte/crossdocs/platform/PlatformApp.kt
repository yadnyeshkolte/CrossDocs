package com.yadnyeshkolte.crossdocs.platform

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen

expect fun getPlatformName(): String

@Composable
fun App() {
    when (getPlatformName()) {
        "iOS" -> IOSApp()
    }
}

actual fun getPlatformName(): String = "iOS"