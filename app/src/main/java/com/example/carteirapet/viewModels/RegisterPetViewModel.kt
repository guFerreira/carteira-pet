package com.example.carteirapet.viewModels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.Breed
import com.example.carteirapet.service.AnimalService
import com.example.carteirapet.service.BreedService
import kotlinx.coroutines.launch


open class RegisterPetViewModel(private val animalService: AnimalService, private val breedService: BreedService) : ViewModel() {

    var breedOptions by mutableStateOf<List<Breed>>(emptyList())
    var selectedBreed by mutableStateOf<Breed?>(null)

    var petImageUri by mutableStateOf<Uri?>(null)

    var name by mutableStateOf<String>("")

    var birthDate by mutableStateOf("")

    var microchip by mutableStateOf("")

    val sexes = listOf("Macho", "Fêmea")
    var sex by mutableStateOf("")

    var neutered by mutableStateOf(false)

    var conditions by mutableStateOf("")

    var weight by mutableStateOf("")

    var species by mutableStateOf("dog")


    fun loadBreeds() {
        viewModelScope.launch {
            breedOptions = breedService.getBreeds(species)
        }
    }

    fun changeSpecies(newSpecies: String) {
        viewModelScope.launch {
            species = newSpecies
            breedOptions = breedService.getBreeds(species)
        }
    }

    fun registerPet(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (selectedBreed != null) {
                    val animal = Animal(
                        name = name,
                        birthDate = birthDate,
                        microchip = microchip,
                        breeds = listOf(selectedBreed!!),
                        sex = sex,
                        neutered = neutered,
                        conditions = conditions,
                        weight = weight?.toFloat() ?: 0f,
                        id = 0,
                        species = species
                    )
                    animalService.registerAnimal(animal)
                    onSuccess()
                } else {
                    onError("Erro: Raça não selecionada.")
                }
            } catch (e: Exception) {
                onError("Erro ao criar conta. Tente novamente. " + e.message)
            }
        }
    }

}