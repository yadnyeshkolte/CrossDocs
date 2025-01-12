package com.yadnyeshkolte.crossdocs.ui.screens.mguide.data

import com.yadnyeshkolte.crossdocs.ui.screens.mguide.model.MarkdownExample

val markdownExamples = listOf(
    // Basic Formatting
    MarkdownExample(
        "Basic Formatting",
        "Bold Text",
        "**Bold text**",
        "Bold text"
    ),
    MarkdownExample(
        "Basic Formatting",
        "Italic Text",
        "*Italic text*",
        "Italic text"
    ),
    MarkdownExample(
        "Basic Formatting",
        "Strikethrough",
        "~~Strikethrough text~~",
        "Strikethrough text"
    ),
    MarkdownExample(
        "Basic Formatting",
        "Highlight Text",
        "==Highlighted text==",
        "Highlighted text"
    ),

    // Headers
    MarkdownExample(
        "Headers",
        "Header 1",
        "# Header 1",
        "Header 1"
    ),
    MarkdownExample(
        "Headers",
        "Header 2",
        "## Header 2",
        "Header 2"
    ),
    MarkdownExample(
        "Headers",
        "Header 3",
        "### Header 3",
        "Header 3"
    ),

    // Lists
    MarkdownExample(
        "Lists",
        "Unordered List",
        """
        * Item 1
        * Item 2
        * Item 3
        """.trimIndent(),
        "• Item 1\n• Item 2\n• Item 3"
    ),
    MarkdownExample(
        "Lists",
        "Ordered List",
        """
        1. First item
        2. Second item
        3. Third item
        """.trimIndent(),
        "1. First item\n2. Second item\n3. Third item"
    ),

    // Task Lists
    MarkdownExample(
        "Task Lists",
        "Task List",
        """
        - [ ] Unchecked task
        - [x] Checked task
        """.trimIndent(),
        "☐ Unchecked task\n☑ Checked task"
    ),

    // Blockquotes
    MarkdownExample(
        "Blockquotes",
        "Simple Blockquote",
        "> This is a blockquote",
        "This is a blockquote (with left border)"
    ),

    // Code
    MarkdownExample(
        "Code",
        "Inline Code",
        "`inline code`",
        "inline code (with background)"
    ),
    MarkdownExample(
        "Code",
        "Code Block",
        """
        ```python
        def hello():
            print("Hello, World!")
        ```
        """.trimIndent(),
        "Code block with syntax highlighting"
    ),

    // Tables
    MarkdownExample(
        "Tables",
        "Simple Table",
        """
        | Header 1 | Header 2 |
        |----------|----------|
        | Cell 1   | Cell 2   |
        | Cell 3   | Cell 4   |
        """.trimIndent(),
        "Formatted table with headers and cells"
    ),

    MarkdownExample(
        "Tables",
        "4x4 Table",
        """
| Language |     Type     | Popular Library |   Common Use Case      |
|:--------:|:-----------:|:---------------:|:-----------------:|
| Java     |  Compiled    |  Spring Boot    | Backend Development |
| Kotlin   |  Compiled    |      Ktor       | Android Development |
| Python   | Interpreted  |     Django      |   Data Science     |
| C#       |  Compiled    |    ASP.NET      |  Game Development   |
        """.trimIndent(),
        "Formatted table with headers and cells"
    ),

    // Super/Subscript
    MarkdownExample(
        "Super/Subscript",
        "Superscript",
        "X^2^",
        "X²"
    ),
    MarkdownExample(
        "Super/Subscript",
        "Subscript",
        "H~2~O",
        "H₂O"
    ),

    // Emojis
    MarkdownExample(
        "Emojis",
        "Smile Emoji",
        ":smile:",
        "😄"
    ),
    MarkdownExample(
        "Emojis",
        "Thumbs Up Emoji",
        ":thumbsup:",
        "👍"
    ),
    MarkdownExample(
        "Emojis",
        "Heart Emoji",
        ":heart:",
        "❤️"
    ),
    MarkdownExample(
        "Emojis",
        "Fire Emoji",
        ":fire:",
        "🔥"
    ),
    MarkdownExample(
        "Emojis",
        "Rocket Emoji",
        ":rocket:",
        "🚀"
    )
)