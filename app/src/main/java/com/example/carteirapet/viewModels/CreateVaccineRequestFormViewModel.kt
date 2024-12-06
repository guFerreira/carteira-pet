package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.UpdatedData
import com.example.carteirapet.service.VaccineRequestService
import kotlinx.coroutines.launch

open class CreateVaccineRequestFormViewModel (private val vaccineRequestService: VaccineRequestService) : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
        private set

    var showRedirectToZapSignUrl by mutableStateOf<Boolean>(false)
        private set

    var zapSignUrl by mutableStateOf<String>("")
        private set

    var updatedData by mutableStateOf<UpdatedData?>(null)
        private set

    // Variáveis para cada campo
    var applicationDate by mutableStateOf("")
    var applicationPlace by mutableStateOf("")
    var manufacturer by mutableStateOf("")
    var batchCode by mutableStateOf("")
    var manufacturingDate by mutableStateOf("")
    var expirationDate by mutableStateOf("")
    var nextDoseDate by mutableStateOf("")
    var vaccineId by mutableStateOf(0)

    fun updateVaccineRequest(vaccineRequestId: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                validateFields()
                val data = UpdatedData(
                    applicationDate = applicationDate,
                    applicationPlace = applicationPlace,
                    manufacturer = manufacturer,
                    batchCode = batchCode,
                    manufacturingDate = manufacturingDate,
                    expirationDate = expirationDate,
                    nextDoseDate = nextDoseDate,
                    vaccineId = vaccineId
                )
                isLoading = true
                val vaccineRequestResponse = vaccineRequestService.updateVaccineRequest(vaccineRequestId, data)
                if (vaccineRequestResponse?.signUrl != null) {
                    zapSignUrl = vaccineRequestResponse.signUrl
                }
            } catch (e: Exception) {
                onError("Erro ao criar a solicitação de vacinação: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Função de validação
    private fun validateFields() {
        if (applicationDate.isEmpty()) {
            throw IllegalArgumentException("Data de aplicação é obrigatória")
        }
        if (applicationPlace.isEmpty()) {
            throw IllegalArgumentException("Local de aplicação é obrigatório")
        }
        if (manufacturer.isEmpty()) {
            throw IllegalArgumentException("Fabricante é obrigatório")
        }
        if (batchCode.isEmpty()) {
            throw IllegalArgumentException("Código do lote é obrigatório")
        }
        if (manufacturingDate.isEmpty()) {
            throw IllegalArgumentException("Data de fabricação é obrigatória")
        }
        if (expirationDate.isEmpty()) {
            throw IllegalArgumentException("Data de validade é obrigatória")
        }
        if (nextDoseDate.isEmpty()) {
            throw IllegalArgumentException("Data da próxima dose é obrigatória")
        }
        if (vaccineId == 0) {
            throw IllegalArgumentException("ID da vacina é obrigatório")
        }
    }
}