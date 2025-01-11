package com.yadnyeshkolte.crossdocs.model


sealed class EmojiToken {
    data class Text(val content: String) : EmojiToken()
    data class Emoji(val content: String) : EmojiToken()
}