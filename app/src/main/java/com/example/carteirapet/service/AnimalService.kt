package com.example.carteirapet.service

import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.AnimalRepository
import com.example.carteirapet.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale

class AnimalService(private val animalRepository: AnimalRepository) {
    suspend fun getAnimals(): List<Animal> {
        return animalRepository.getAnimals()
    }

    suspend fun getAnimalById(animalId: Int): Animal? {
        var animal = animalRepository.getAnimalById(animalId)
        if (animal != null) {
            animal.birthDate = DateUtils.formatDateStringToShow(animal.birthDate)
        }
        return animal
    }

    suspend fun registerAnimal(animal: Animal, image: ByteArray?) {
        animal.birthDate = DateUtils.formatDateStringToRegister(animal.birthDate)
        animalRepository.registerAnimal(animal, image)
    }

}