package com.example.carteirapet.service

import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.AnimalRepository
import java.text.SimpleDateFormat
import java.util.Locale

class AnimalService(private val animalRepository: AnimalRepository) {
    suspend fun getAnimals(): List<Animal> {
        return animalRepository.getAnimals()
    }

    suspend fun getAnimalById(animalId: Int): Animal? {
        var animal = animalRepository.getAnimalById(animalId)
        if (animal != null) {
            animal.birthDate = formatDateStringToShow(animal.birthDate)
        }
        return animal
    }

    suspend fun registerAnimal(animal: Animal) {
        animal.birthDate = formatDateStringToRegister(animal.birthDate)
        animalRepository.registerAnimal(animal)
    }

    fun formatDateStringToShow(dateString:String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun formatDateStringToRegister(dateString:String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}