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
import kotlinx.coroutines.launch


open class RegisterPetViewModel(private val animalService: AnimalService) : ViewModel() {

    var petImageUri by mutableStateOf<Uri?>(null)

    var name by mutableStateOf<String>("")

    var birthDate by mutableStateOf("")

    var microchip by mutableStateOf("")

    var breed by mutableStateOf("")

    var sex by mutableStateOf("")

    var neutered by mutableStateOf(false)

    var conditions by mutableStateOf("")

    var weight by mutableStateOf("")

    var species by mutableStateOf("Cachorro")

    val breeds = listOf("Labrador", "Bulldog", "Poodle", "Vira-lata", "Outra")
    val sexes = listOf("Macho", "Fêmea")

    fun registerPet(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val animal = Animal(
                    name = name,
                    birthDate = birthDate,
                    microchip = microchip,
                    breeds = listOf(
                        Breed(id = null, breed)
                    ),
                    sex = sex,
                    neutered = neutered,
                    conditions = conditions,
                    weight = weight?.toFloat() ?: 0f,
                    id = null,
                    species = species
                )
                animalService.registerAnimal(animal)
                onSuccess()
            }catch (e: Exception){
                onError("Erro ao criar conta. Tente novamente." + e.message)
            }
        }
    }
}