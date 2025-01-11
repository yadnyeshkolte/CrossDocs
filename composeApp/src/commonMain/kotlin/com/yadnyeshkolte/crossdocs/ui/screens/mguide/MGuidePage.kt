package com.yadnyeshkolte.crossdocs.ui.screens.mguide


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.yadnyeshkolte.crossdocs.ui.screens.mguide.components.ExampleCard
import com.yadnyeshkolte.crossdocs.ui.screens.mguide.data.markdownExamples

@Composable
fun MGuidePage() {
    val clipboardManager = LocalClipboardManager.current
    var selectedCategory by remember { mutableStateOf("Basic Formatting") }
    val categories = markdownExamples.map { it.category }.distinct()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Markdown Guide",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory),
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary
        ) {
            categories.forEach { category ->
                Tab(
                    selected = category == selectedCategory,
                    onClick = { selectedCategory = category },
                    text = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(markdownExamples.filter { it.category == selectedCategory }) { example ->
                ExampleCard(
                    example = example,
                    onCopyClick = {
                        clipboardManager.setText(AnnotatedString(example.markdownSyntax))
                    }
                )
            }
        }
    }
}