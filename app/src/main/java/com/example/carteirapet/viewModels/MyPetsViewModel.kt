package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.UserResponse
import com.example.carteirapet.service.AnimalService
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.UserService
import kotlinx.coroutines.launch


open class MyPetsViewModel(private val authService: AuthService, private val userService: UserService, private val animalService: AnimalService) : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(true)
        private set

    var name by mutableStateOf<String>("")
        private set

    var userProfile by mutableStateOf<UserResponse?>(null)
        private set

    var animals by mutableStateOf<List<Animal>>(emptyList())
        private set

    fun setIsLoading(value: Boolean){
        isLoading = value
    }

    fun loadUserProfile(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                userProfile = userService.getUserInformations()
                name = userProfile?.petGuardian?.firstName
                    ?: userProfile?.veterinaryDoctor?.lastName
                            ?: ""
            } catch (e: Exception) {
                onError("Erro ao buscar perfil do usuário: ${e.message}")
            }
        }
    }
    fun loadAnimals(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                animals = animalService.getAnimals()
            } catch (e: Exception) {
                onError("Erro ao buscar os pets: ${e.message}")
            }
        }
    }

    fun logout(onHomePageNavigate: () -> Unit, onError: (String) -> Unit) {
        // Lança a operação de login
        viewModelScope.launch {
            try {
                authService.logout()
                onHomePageNavigate()
            } catch (e: Exception) {
                onError("Erro ao fazer logout: ${e.message}")
            }
        }
    }

}