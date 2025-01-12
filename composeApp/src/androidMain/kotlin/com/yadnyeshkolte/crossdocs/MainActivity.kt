package com.yadnyeshkolte.crossdocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yadnyeshkolte.crossdocs.platform.AndroidApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AndroidApp()
}