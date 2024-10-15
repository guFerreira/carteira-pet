package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.service.AuthService
import kotlinx.coroutines.launch

// Estado do login
sealed class LoginState {
    object Idle : LoginState() // Estado inicial
    object Loading : LoginState() // Estado de carregamento
    data class Success(val message: String) : LoginState() // Estado de sucesso
    data class Error(val error: String) : LoginState() // Estado de erro
}

open class LoginViewModel(private val authService: AuthService) : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    // Estado para o login (Loading, Success, Error)
    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun onUsernameChanged(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChanged(newPassword: String) {
        password = newPassword
    }

    // Estado que valida se o login pode ser habilitado
    val isLoginEnabled: Boolean
        get() = username.isNotEmpty() && password.isNotEmpty()

    fun login(onSuccess: () -> Unit) {
        // Define o estado como carregando
        loginState = LoginState.Loading

        // Lança a operação de login
        viewModelScope.launch {
            try {
                val isLoginSuccess = authService.login(username, password)
                if (isLoginSuccess) {
                    loginState = LoginState.Success("Login realizado com sucesso!")
                    onSuccess()
                } else {
                    loginState = LoginState.Error("Credenciais inválidas.")
                }
            } catch (e: Exception) {
                loginState = LoginState.Error("Erro ao realizar login: ${e.message}")
            }
        }
    }
}
