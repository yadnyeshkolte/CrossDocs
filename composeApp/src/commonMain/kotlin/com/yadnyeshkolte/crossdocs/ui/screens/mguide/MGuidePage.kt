package com.yadnyeshkolte.crossdocs.ui.screens.mguide


import androidx.compose.foundation.background
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
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        Text(
            "Markdown Guide",
            style = MaterialTheme.typography.h4.copy(
                color = MaterialTheme.colors.onBackground
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Updated ScrollableTabRow with theme-aware colors
        Surface(
            elevation = 4.dp,
            color = MaterialTheme.colors.surface
        ) {
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary,
                edgePadding = 16.dp
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = category == selectedCategory,
                        onClick = { selectedCategory = category },
                        text = {
                            Text(
                                text = category,
                                color = if (category == selectedCategory)
                                    MaterialTheme.colors.primary
                                else
                                    MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(MaterialTheme.colors.background)
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