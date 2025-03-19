package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.delete
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
data class AuthCredential(val accessToken: String, val refreshToken: String)

@Serializable
data class RefreshedToken(val accessToken: String)

@Serializable
data class RefreshTokenRequest(val token: String)

@Serializable
data class UserRequest(val username: String, val password: String)

class AuthRepository(private val client: HttpClient) {
    private val url = "10.0.2.2:3000";
    suspend fun login(username: String, password: String): AuthCredential? {
        val response: HttpResponse = client.post("http://${url}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val loginResponse = response.body<AuthCredential>()
            return loginResponse
        } else {
            null  // Login falhou
        }
    }

    suspend fun refreshAccessToken(token: String): RefreshedToken? {
        val response: HttpResponse = client.post("http://${url}/auth/refresh/token") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(token))
        }
        return if (response.status == HttpStatusCode.OK) {
            return response.body<RefreshedToken>()
        } else {
            null
        }
    }

    suspend fun registerUser(username: String, password: String): Boolean {
        val response: HttpResponse = client.post("http://${url}/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(UserRequest(username, password))
        }
        return if (response.status == HttpStatusCode.Created) {
            return true
        } else {
            return false
        }
    }

    suspend fun logout():Boolean {
        val response: HttpResponse = client.delete("http://${url}/auth/logout")
        return if (response.status == HttpStatusCode.OK) {
            client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
                .firstOrNull()?.clearToken()
            return true
        } else {
            return false
        }
    }
}