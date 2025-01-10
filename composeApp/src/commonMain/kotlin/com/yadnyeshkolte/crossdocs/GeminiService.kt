package com.yadnyeshkolte.crossdocs.gemini

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*

class GeminiService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                encodeDefaults = true
            })
        }
    }

    suspend fun generateResponse(prompt: String): String {
        return try {
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = prompt)
                        )
                    )
                )
            )

            val response: HttpResponse = client.post("${GeminiConfig.API_URL}?key=${GeminiConfig.API_KEY}") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val geminiResponse: GeminiResponse = response.body()
                    geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "No response generated"
                }
                else -> {
                    "Error: ${response.status.description}"
                }
            }
        } catch (e: Exception) {
            println("Error generating response: ${e.message}")
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }

    fun dispose() {
        client.close()
    }
}
