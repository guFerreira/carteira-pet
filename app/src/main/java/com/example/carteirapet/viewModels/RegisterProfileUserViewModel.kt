package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Address
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.UserService
import kotlinx.coroutines.launch


open class RegisterProfileUserViewModel (private val authService: AuthService, private val userService: UserService) : ViewModel() {

    // Step management
    var currentStep by mutableStateOf(1)
        private set
    // Personal information (Step 1)
    var isVet by mutableStateOf(true)
        private set
    var isRegistered by mutableStateOf(false)
        private set
    var firstName by mutableStateOf("")
        private set
    var lastName by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var cpf by mutableStateOf("")
        private set

    // Address information (Step 2)
    var cep by mutableStateOf("")
        private set
    var street by mutableStateOf("")
        private set
    var number by mutableStateOf("")
        private set
    var complement by mutableStateOf("")
        private set
    var city by mutableStateOf("")
        private set
    var state by mutableStateOf("")
        private set

    // Methods to update the state
    fun updateFirstName(value: String) {
        firstName = value
    }

    fun updateLastName(value: String) {
        lastName = value
    }

    fun updatePhoneNumber(value: String) {
        phoneNumber = value
    }

    fun updateEmail(value: String) {
        email = value
    }

    fun updateCpf(value: String) {
        cpf = value
    }

    fun updateIsVet(value: Boolean) {
        isVet = value
    }

    fun updateCep(value: String) {
        cep = value
    }

    fun updateStreet(value: String) {
        street = value
    }

    fun updateNumber(value: String) {
        number = value
    }

    fun updateComplement(value: String) {
        complement = value
    }

    fun updateCity(value: String) {
        city = value
    }

    fun updateState(value: String) {
        state = value
    }

    fun goToNextStep() {
        if (currentStep < 2) currentStep++
    }

    fun goToPreviousStep() {
        if (currentStep > 1) currentStep--
    }

    fun logout(onLogout: () -> Unit){
        authService.logout()
        onLogout()
    }

    fun registerProfileData(onRegister: () -> Unit, onError: (String) -> Unit){
        val address =  Address(cep.toInt(), street, number.toInt(), complement, city, state)
        val profile = Profile(isVet, isRegistered, firstName, lastName, email, phoneNumber, cpf, address)

        // Lança a operação de login
        viewModelScope.launch {
            try {
                userService.registerUserProfile(profile)
                onRegister()
            } catch (e: Exception) {
                onError("Erro ao realizar cadastro")
            }
        }

    }
}