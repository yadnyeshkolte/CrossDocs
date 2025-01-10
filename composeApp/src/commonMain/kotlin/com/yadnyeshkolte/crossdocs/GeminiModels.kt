package com.yadnyeshkolte.crossdocs.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig(),
    val safetySettings: List<SafetySetting> = defaultSafetySettings
)

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerationConfig(
    val temperature: Float = 0.7f,
    @SerialName("topK")
    val topK: Int = 40,
    @SerialName("topP")
    val topP: Float = 0.95f,
    val maxOutputTokens: Int = 1024
)

@Serializable
data class SafetySetting(
    val category: String,
    val threshold: String
)

@Serializable
data class GeminiResponse(
    val candidates: List<Candidate> = emptyList(),
    val promptFeedback: PromptFeedback? = null,
    @SerialName("usageMetadata")
    val usageMetadata: UsageMetadata? = null
)

@Serializable
data class UsageMetadata(
    val promptTokenCount: Int,
    val candidatesTokenCount: Int,
    val totalTokenCount: Int
)

@Serializable
data class Candidate(
    val content: ContentResponse,
    val finishReason: String? = null,
    val index: Int = 0,
    val safetyRatings: List<SafetyRating> = emptyList()
)

@Serializable
data class ContentResponse(
    val parts: List<Part>,
    val role: String? = null
)

@Serializable
data class PromptFeedback(
    val safetyRatings: List<SafetyRating> = emptyList()
)

@Serializable
data class SafetyRating(
    val category: String,
    val probability: String
)

val defaultSafetySettings = listOf(
    SafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_NONE"),
    SafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_NONE"),
    SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_NONE"),
    SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_NONE")
)
