package com.example.carteirapet.service

import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineRepository

class VaccineService(private val vaccineRepository: VaccineRepository) {
    suspend fun getVaccineRequestByAnimalId(animalId: Int): List<Vaccine> {
        return vaccineRepository.getVaccineRequestByAnimalId(animalId)
    }

    suspend fun getVaccineRequestFromVecterinary(): List<Vaccine> {
        return vaccineRepository.getVaccineRequestFromVecterinary()
    }


}