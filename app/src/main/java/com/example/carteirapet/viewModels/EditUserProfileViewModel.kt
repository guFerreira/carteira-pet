package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Address
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.ProfileCreateResponse
import com.example.carteirapet.service.UserService
import kotlinx.coroutines.launch

class EditUserProfileViewModel(private val userService: UserService) : ViewModel() {
    var isLoading by mutableStateOf(false)
    // Personal information
    var isRegistered by mutableStateOf(true)
    var isVet by mutableStateOf(false)
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var email by mutableStateOf("")
    var cpf by mutableStateOf("")
    var crmv by mutableStateOf("")

    // Address information
    var cep by mutableStateOf("")
    var street by mutableStateOf("")
    var number by mutableStateOf("")
    var complement by mutableStateOf("")
    var city by mutableStateOf("")
    var state by mutableStateOf("")


    fun loadUserProfile(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                var userProfile = userService.getUserInformations()
                if (userProfile == null) {
                    onError("Erro ao buscar perfil do usuário")
                    return@launch
                }
                isVet =  userProfile.isVet
                isRegistered = userProfile.isRegistered
                firstName = userProfile.firstName
                lastName = userProfile.lastName
                phoneNumber = userProfile.phoneNumber
                email = userProfile.email
                cpf = userProfile.cpf

                cep = userProfile.address.cep.toString()
                street = userProfile.address.street
                number = userProfile.address.number.toString()
                complement = userProfile.address.complement
                city = userProfile.address.city
                state = userProfile.address.state
                if (isVet && userProfile.crmv != null){
                    crmv = userProfile.crmv!!
                }


            } catch (e: Exception) {
                onError("Erro ao buscar perfil do usuário: ${e.message}")
            }
        }
    }

    fun updateProfileData(onRegister: () -> Unit, onError: (String) -> Unit) {
        val address =
            Address(cep.toInt(), street, number.toInt(), complement, city, state)
        val profile = ProfileCreateResponse(isVet, crmv, isRegistered, firstName, lastName, email, phoneNumber, cpf, address)

        // Lança a operação de login
        viewModelScope.launch {
            try {
                userService.updaterUserProfile(profile)
                onRegister()
            } catch (e: Exception) {
                onError("Erro ao realizar cadastro")
            }
        }

    }
}