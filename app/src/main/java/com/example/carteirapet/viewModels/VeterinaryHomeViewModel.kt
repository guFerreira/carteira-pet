package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.service.AnimalService
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.VaccineRequestService
import com.example.carteirapet.service.VaccineService
import kotlinx.coroutines.launch


open class VeterinaryHomeViewModel (private val authService: AuthService, private val vaccineRequestService: VaccineRequestService) : ViewModel() {
    var vaccines by mutableStateOf<List<VaccineRequestByVeterinary>>(emptyList())
    var isLoading by mutableStateOf<Boolean>(false)

    fun loadVaccineRequestsFromVeterinary(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                vaccines = vaccineRequestService.getAllVaccineRequestFromVeterinary()
            } catch (e: Exception) {
                onError("Erro ao buscar vacinas do veterinário: ${e.message}")
            }
        }
    }

    fun logout(onHomePageNavigate: () -> Unit, onError: (String) -> Unit) {
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