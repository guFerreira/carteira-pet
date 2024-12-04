package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.service.VaccineRequestService
import kotlinx.coroutines.launch

open class CreateVaccineRequestViewModel (private val vaccineRequestService: VaccineRequestService ) : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
        private set

    fun createVaccineRequest(petId: Int, onSuccessful: (id: Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                val vaccineRequest = vaccineRequestService.createVaccineRequest(petId)
                if (vaccineRequest != null) {
                    onSuccessful(vaccineRequest.id)
                }
            } catch (e: Exception) {
                onError("Erro ao criar a solicitação de vacinação ${e.message}")
            } finally {
                isLoading = false
            }
        }

    }
}