package com.example.carteirapet.service

import com.example.carteirapet.repositories.AuthRepository

class AuthService(private val authRepository: AuthRepository,     private val tokenManagerService: TokenManagerService) {
    suspend fun login(username: String, password: String): Boolean {
        val loginResponse = authRepository.login(username, password)
        return if (loginResponse != null) {
            tokenManagerService.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
            true
        } else {
            false // Login falhou
        }
    }

    fun getAccessToken(): String? {
        return tokenManagerService.getAccessToken()
    }

    fun logout() {
        tokenManagerService.clearTokens()
    }
}
