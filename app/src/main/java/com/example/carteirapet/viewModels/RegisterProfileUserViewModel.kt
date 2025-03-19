package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Address
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.ProfileCreateResponse
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.CepService
import com.example.carteirapet.service.UserService
import kotlinx.coroutines.launch


open class RegisterProfileUserViewModel (private val authService: AuthService, private val userService: UserService, private val cepService: CepService) : ViewModel() {

    // Step management
    var currentStep by mutableStateOf(1)
        private set
    // Personal information (Step 1)
    var isVet by mutableStateOf(false)
        private set
    var crmv by mutableStateOf("")
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
    var isSearchingCep by mutableStateOf(false)
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

    fun updateCrmv(value: String) {
        crmv = value
    }

    fun updateCep(value: String) {
        cep = value
        if(cep.length == 8) {
            viewModelScope.launch {
                try {
                    isSearchingCep = true
                    val address = cepService.getCep(cep)
                    updateStreet(address.logradouro)
                    updateCity(address.localidade)
                    updateState(address.uf)
                    isSearchingCep = false
                } catch (e: Exception) {
                    // Handle error
                    println(e)
                    isSearchingCep = false
                    updateStreet("")
                    updateCity("")
                    updateState("")
                }
            }
        }
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

    fun validateRequiredFieldsInFirstStep(): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty() && cpf.isNotEmpty() && (!isVet || crmv.isNotEmpty())
    }


    fun validateRequiredFieldsInSecondStep(): Boolean {
        return cep.isNotEmpty() && street.isNotEmpty() && number.isNotEmpty() && complement.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty()
    }

    fun logout(onLogout: () -> Unit, onError: (String) -> Unit){
        viewModelScope.launch {
            try {
                authService.logout()
                onLogout()
            } catch (e: Exception) {
                onError("Erro ao realizar logout")
            }
        }
    }

    fun registerProfileData(onRegister: () -> Unit, onError: (String) -> Unit){
        val address =  Address(cep.toInt(), street, number.toInt(), complement, city, state)
        val profile = ProfileCreateResponse(isVet, crmv, isRegistered, firstName, lastName, email, phoneNumber, cpf, address)

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