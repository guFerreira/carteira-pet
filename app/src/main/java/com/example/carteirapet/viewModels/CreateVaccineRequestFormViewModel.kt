package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Breed
import com.example.carteirapet.repositories.UpdatedData
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.service.VaccineRequestService
import com.example.carteirapet.service.VaccineService
import kotlinx.coroutines.launch

open class CreateVaccineRequestFormViewModel (private val vaccineRequestService: VaccineRequestService, private val vaccineService: VaccineService) : ViewModel() {
    var isLoading by mutableStateOf<Boolean>(false)
        private set

    var isEditing by mutableStateOf<Boolean>(true)

    var showRedirectToZapSignUrl by mutableStateOf<Boolean>(false)
        private set

    var zapSignUrl by mutableStateOf<String>("")
        private set

    var updatedData by mutableStateOf<UpdatedData?>(null)
        private set

    var vaccineOptions by mutableStateOf<List<Vaccine>>(emptyList())

    var applicationDate by mutableStateOf("")
    var applicationPlace by mutableStateOf("")
    var manufacturer by mutableStateOf("")
    var batchCode by mutableStateOf("")
    var manufacturingDate by mutableStateOf("")
    var expirationDate by mutableStateOf("")
    var nextDoseDate by mutableStateOf("")
    var selectedVaccine by mutableStateOf<Vaccine?>(null)

    fun loadData(vaccineRequestId: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val vaccineRequest = vaccineRequestService.getVaccineRequestsFromVeterinaryById(vaccineRequestId)
                if (vaccineRequest == null) {
                    onError("Erro ao buscar os dados da solicitação de vacina")
                    return@launch
                }

                if (vaccineRequest.animalSpecies != null) {
                    vaccineOptions = vaccineService.getAllVaccinesBySpecies(vaccineRequest.animalSpecies)
                }

                if (vaccineRequest.signedUrl != null) {
                    isEditing = false
                    zapSignUrl = vaccineRequest.signedUrl
                }

                applicationDate =  vaccineRequest.applicationDate ?: ""
                applicationPlace =  vaccineRequest.applicationPlace ?: ""
                manufacturer = vaccineRequest.manufacturer ?: ""
                manufacturingDate = vaccineRequest.manufacturingDate ?: ""
                expirationDate = vaccineRequest.expirationDate ?: ""
                batchCode = vaccineRequest.batchCode ?: ""
                nextDoseDate = vaccineRequest.nextDoseDate ?: ""
                selectedVaccine = vaccineRequest.vaccine
            } catch (e: Exception) {
                onError("Erro ao buscar perfil do usuário: ${e.message}")
            }
        }
    }

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
                    vaccineId = selectedVaccine!!.id
                )
                isLoading = true
                val vaccineRequestResponse = vaccineRequestService.updateVaccineRequest(vaccineRequestId, data)
                if (vaccineRequestResponse?.signUrl != null) {
                    zapSignUrl = vaccineRequestResponse.signUrl
                    isEditing = false
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
        if (this.manufacturer.isEmpty()) {
            throw IllegalArgumentException("Fabricante é obrigatório")
        }
        if (batchCode.isEmpty()) {
            throw IllegalArgumentException("Código do lote é obrigatório")
        }
        if (this.manufacturer.isEmpty()) {
            throw IllegalArgumentException("Data de fabricação é obrigatória")
        }
        if (expirationDate.isEmpty()) {
            throw IllegalArgumentException("Data de validade é obrigatória")
        }
        if (nextDoseDate.isEmpty()) {
            throw IllegalArgumentException("Data da próxima dose é obrigatória")
        }
        if (selectedVaccine == null) {
            throw IllegalArgumentException("ID da vacina é obrigatório")
        }
    }
}