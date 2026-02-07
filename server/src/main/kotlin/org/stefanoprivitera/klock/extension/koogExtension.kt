package org.stefanoprivitera.klock.extension

import ai.koog.ktor.Koog
import ai.koog.ktor.mcp
import io.ktor.server.application.*

//fun Application.koogExtension() {
//    install(Koog) {
//        agentConfig {
//            mcp {
//                // Register via SSE
//                sse("https://your-mcp-server.com/sse")
//            }
//        }
//        llm {
//            openAI(apiKey = "your-openai-api-key")
//            anthropic(apiKey = "your-anthropic-api-key")
//            ollama { baseUrl = "http://localhost:11434" }
//            google(apiKey = "your-google-api-key")
//            openRouter(apiKey = "your-openrouter-api-key")
//            deepSeek(apiKey = "your-deepseek-api-key")
//        }
//    }
//}