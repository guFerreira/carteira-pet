package com.example.carteirapet.service

import com.example.carteirapet.repositories.Cep
import com.example.carteirapet.repositories.CepRepository
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.UserRepository

class CepService(private val cepRepository: CepRepository) {
    suspend fun getCep(cep: String): Cep {
        return cepRepository.getCep(cep)
    }
}