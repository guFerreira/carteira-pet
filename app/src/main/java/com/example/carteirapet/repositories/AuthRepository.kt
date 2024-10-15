package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

// Data class para armazenar os dados do login
@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val accessToken: String, val refreshToken: String)

class AuthRepository(private val client: HttpClient) {
    suspend fun login(username: String, password: String): LoginResponse? {
        val response: HttpResponse = client.post("http://35.232.119.76/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val loginResponse = response.body<LoginResponse>()
            return loginResponse
        } else {
            null  // Login falhou
        }
    }
}