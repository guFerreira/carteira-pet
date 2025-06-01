package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.exceptions.VaccineRequestNotFoundException
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.service.VaccineRequestService
import kotlinx.coroutines.launch

open class CreateVaccineRequestViewModel (private val vaccineRequestService: VaccineRequestService ) : ViewModel() {
    var isCreatingVaccineRequest by mutableStateOf<Boolean>(false)
        private set
    var isLoadingActiveVaccineRequest by mutableStateOf<Boolean>(false)
        private set
    var vaccineRequest by mutableStateOf<VaccineRequestResponse?>(null)

    fun createVaccineRequest(petId: Int, onSuccessful: (id: Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isCreatingVaccineRequest = true
                val createdVaccineRequest = vaccineRequestService.createVaccineRequest(petId)
                vaccineRequest = createdVaccineRequest
                onSuccessful(createdVaccineRequest.id)
            } catch (e: Exception) {
                onError("Erro ao criar a solicitação de vacinação ${e.message}")
            } finally {
                isCreatingVaccineRequest = false
            }
        }
    }

    fun loadActiveVaccineRequestByAnimal(petId: Int, onSuccessful: (id: Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isLoadingActiveVaccineRequest = true
                val activeVaccineRequestResponse = vaccineRequestService.getActiveVaccineRequestByAnimalId(petId)
                vaccineRequest = activeVaccineRequestResponse
                onSuccessful(activeVaccineRequestResponse.id)
            } catch (e: VaccineRequestNotFoundException) {
                vaccineRequest = null
            } catch (e: Exception) {
                onError("Erro ao buscar a a solicitação de vacinação ativa ${e.message}")
            } finally {
                isLoadingActiveVaccineRequest = false
            }
        }
    }
}