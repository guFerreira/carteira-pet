package com.example.carteirapet.service

import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineRepository

class VaccineService(private val vaccineRepository: VaccineRepository) {
    suspend fun getAllVaccines(): List<Vaccine> {
        return vaccineRepository.getAllVaccines()
    }
}