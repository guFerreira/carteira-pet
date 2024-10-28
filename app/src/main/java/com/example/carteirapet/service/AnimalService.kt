package com.example.carteirapet.service

import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.AnimalRepository

class AnimalService(private val animalRepository: AnimalRepository) {
    suspend fun getAnimals(): List<Animal> {
        return animalRepository.getAnimals()
    }

    suspend fun getAnimalById(animalId: Int): Animal? {
        return animalRepository.getAnimalById(animalId)
    }

    suspend fun registerAnimal(animal: Animal) {
        animalRepository.registerAnimal(animal)
    }
}