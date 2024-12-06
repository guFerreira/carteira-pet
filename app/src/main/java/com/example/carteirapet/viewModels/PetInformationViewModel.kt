package com.example.carteirapet.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.VaccineRequestByAnimal
import com.example.carteirapet.service.AnimalService
import com.example.carteirapet.service.VaccineRequestService
import kotlinx.coroutines.launch

open class PetInformationViewModel (private val animalService: AnimalService, private val vaccineRequestService: VaccineRequestService) : ViewModel() {
    var pet by mutableStateOf<Animal?>(null)
    var vaccineRequests by mutableStateOf<List<VaccineRequestByAnimal>>(emptyList())
    var isLoading by mutableStateOf<Boolean>(false)

    fun loadPetInformation(petId: Int, onError: (String) -> Unit) {
        // Lança a operação de login
        viewModelScope.launch {
            try {
                pet = animalService.getAnimalById(petId)
            } catch (e: Exception) {
                onError("Erro ao realizar cadastro")
            }
        }

    }

    fun loadVaccinesByPetId(petId: Int, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                vaccineRequests = vaccineRequestService.getAllVaccineRequestByAnimalId(petId)
            } catch (e: Exception) {
                onError("Erro ao realizar cadastro")
            }
        }

    }
}