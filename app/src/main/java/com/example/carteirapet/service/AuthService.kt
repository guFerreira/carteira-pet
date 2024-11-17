package com.example.carteirapet.service

import com.example.carteirapet.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthService(private val authRepository: AuthRepository, private val tokenManagerService: TokenManagerService) {
    suspend fun login(username: String, password: String): Boolean {
        val loginResponse = authRepository.login(username, password)
        return if (loginResponse != null) {
            tokenManagerService.saveTokens(loginResponse.accessToken, loginResponse.refreshToken)
            true
        } else {
            false // Login falhou
        }
    }

    suspend fun refreshAccessToken(): Boolean {
        val refreshToken = tokenManagerService.getRefreshToken()
        if (refreshToken == null) {
            return false // Refresh token não disponível
        }
        val newAccessToken = authRepository.refreshAccessToken(refreshToken)
        if (newAccessToken != null && newAccessToken.accessToken != null){
            tokenManagerService.saveTokens(newAccessToken.accessToken, refreshToken)
            return true
        } else {
            return false
        }
    }

    suspend fun registerUser(username: String, password: String): Boolean {
        val isRegisterUserSuccess = authRepository.registerUser(username, password)
        return isRegisterUserSuccess
    }

    fun getAccessToken(): String? {
        return tokenManagerService.getAccessToken()
    }

    fun getRefreshToken(): String? {
        return tokenManagerService.getRefreshToken()
    }

    suspend fun logout() {
        val isSuccess = authRepository.logout()
        if (isSuccess) { tokenManagerService.clearTokens() }
    }
}
