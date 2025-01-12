
package com.yadnyeshkolte.crossdocs.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.yadnyeshkolte.crossdocs.parser.EmojiParser

@Composable
fun RichTextWithEmoji(
    text: String,
    style: TextStyle = TextStyle(fontSize = 14.sp)
) {
    Text(
        text = processTextFormattingWithEmoji(text),
        style = style
    )
}

private fun processTextFormattingWithEmoji(text: String) = buildAnnotatedString {
    val processedText = EmojiParser.parseEmojis(text)

    var currentPos = 0
    while (currentPos < processedText.length) {
        // Handle bold (**text**)
        if (processedText.startsWith("**", currentPos)) {
            val endPos = processedText.indexOf("**", currentPos + 2)
            if (endPos != -1) {
                append(processedText.substring(currentPos, currentPos + 2))
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(processedText.substring(currentPos + 2, endPos))
                pop()
                currentPos = endPos + 2
                continue
            }
        }

        // Handle italic (*text*)
        if (processedText.startsWith("*", currentPos) && !processedText.startsWith("**", currentPos)) {
            val endPos = processedText.indexOf("*", currentPos + 1)
            if (endPos != -1) {
                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                append(processedText.substring(currentPos + 1, endPos))
                pop()
                currentPos = endPos + 1
                continue
            }
        }

        // Handle code (`text`)
        if (processedText.startsWith("`", currentPos)) {
            val endPos = processedText.indexOf("`", currentPos + 1)
            if (endPos != -1) {
                pushStyle(SpanStyle(
                    fontFamily = FontFamily.Monospace,
                    background = Color.LightGray.copy(alpha = 0.3f)
                ))
                append(processedText.substring(currentPos + 1, endPos))
                pop()
                currentPos = endPos + 1
                continue
            }
        }

        // Handle strikethrough (~~text~~)
        if (processedText.startsWith("~~", currentPos)) {
            val endPos = processedText.indexOf("~~", currentPos + 2)
            if (endPos != -1) {
                pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                append(processedText.substring(currentPos + 2, endPos))
                pop()
                currentPos = endPos + 2
                continue
            }
        }

        // Handle subscript (~text~)
        if (processedText.startsWith("~", currentPos) && !processedText.startsWith("~~", currentPos)) {
            val endPos = processedText.indexOf("~", currentPos + 1)
            if (endPos != -1) {
                pushStyle(SpanStyle(
                    baselineShift = BaselineShift.Subscript,
                    fontSize = (14 * 0.75).sp
                ))
                append(processedText.substring(currentPos + 1, endPos))
                pop()
                currentPos = endPos + 1
                continue
            }
        }

        // Handle superscript (^text^)
        if (processedText.startsWith("^", currentPos)) {
            val endPos = processedText.indexOf("^", currentPos + 1)
            if (endPos != -1) {
                pushStyle(SpanStyle(
                    baselineShift = BaselineShift.Superscript,
                    fontSize = (14 * 0.75).sp
                ))
                append(processedText.substring(currentPos + 1, endPos))
                pop()
                currentPos = endPos + 1
                continue
            }
        }

        // Handle highlight (==text==)
        if (processedText.startsWith("==", currentPos)) {
            val endPos = processedText.indexOf("==", currentPos + 2)
            if (endPos != -1) {
                pushStyle(SpanStyle(
                    background = Color(0xFF7F52FF).copy(alpha = 0.3f)
                ))
                append(processedText.substring(currentPos + 2, endPos))
                pop()
                currentPos = endPos + 2
                continue
            }
        }

        // If no special formatting is found, append the current character and move forward
        append(processedText[currentPos].toString())
        currentPos++
    }
}