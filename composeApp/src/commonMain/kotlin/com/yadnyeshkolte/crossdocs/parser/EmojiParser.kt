package com.yadnyeshkolte.crossdocs.parser

object EmojiParser {
    private val emojiMap = mapOf(
        ":smile:" to "🙂",
        ":laughing:" to "😄",
        ":heart:" to "❤",
        ":thumbsup:" to "👍",
        ":thumbsdown:" to "👎",
        ":star:" to "⭐",
        ":fire:" to "🔥",
        ":warning:" to "⚠",
        ":check:" to "✓",
        ":x:" to "✗",
        ":rocket:" to "🚀",
        ":eyes:" to "👀",
        ":thinking:" to "🤔",
        ":100:" to "💯"
    )

    private val emojiRegex = ":([a-z0-9_+-]+):".toRegex()

    fun parseEmojis(text: String): String {
        var result = text
        emojiRegex.findAll(text).forEach { matchResult ->
            val emojiCode = matchResult.value
            val emoji = emojiMap[emojiCode] ?: emojiCode
            result = result.replace(emojiCode, emoji)
        }
        return result
    }
}