package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.service.VaccineRequestService
import kotlinx.coroutines.launch

class AcceptOrRejectVaccineRequestViewModel (private val vaccineRequestService: VaccineRequestService) : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
        private set

    fun rejectVaccineRequest(vaccineRequestId: Int, onSuccessful: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                vaccineRequestService.rejectVaccineRequest(vaccineRequestId)
                onSuccessful()
            } catch (e: Exception) {
                onError("Erro ao criar a solicitação de vacinação ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun acceptVaccineRequest(vaccineRequestId: Int, onSuccessful: (id: Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                vaccineRequestService.acceptVaccineRequest(vaccineRequestId)
                onSuccessful(vaccineRequestId)
            } catch (e: Exception) {
                onError("Erro ao criar a solicitação de vacinação ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}