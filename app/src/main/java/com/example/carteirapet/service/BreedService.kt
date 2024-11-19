package com.example.carteirapet.service

import com.example.carteirapet.repositories.Breed
import com.example.carteirapet.repositories.BreedRepository

class BreedService(private val breedRepository: BreedRepository) {

    suspend fun getBreeds(specie: String): List<Breed> {
       return breedRepository.getBreeds(specie)
    }
}