package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.service.AuthService
import kotlinx.coroutines.launch


open class SignupViewModel(private val authService: AuthService) : ViewModel() {
    // Estado do usuário
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmationPassword by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var isConfirmationPasswordVisible by mutableStateOf(false)
        private set

    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateConfirmationPassword(newConfirmationPassword: String) {
        confirmationPassword = newConfirmationPassword
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun toggleConfirmationPasswordVisibility() {
        isConfirmationPasswordVisible = !isConfirmationPasswordVisible
    }

    fun createAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (password == confirmationPassword) {
                val result = authService.registerUser(username, password)
                if (result) {
                    onSuccess()
                } else {
                    onError("Erro ao criar conta. Tente novamente.")
                }
            } else {
                onError("As senhas não coincidem.")
            }
        }
    }
}