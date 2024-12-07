package com.example.carteirapet.service

import com.example.carteirapet.repositories.CreateVaccineRequest
import com.example.carteirapet.repositories.CreateVaccineRequestResponse
import com.example.carteirapet.repositories.UpdateVaccineRequest
import com.example.carteirapet.repositories.UpdateVaccineRequestResponse
import com.example.carteirapet.repositories.UpdatedData
import com.example.carteirapet.repositories.VaccineRequestByAnimal
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.repositories.VaccineRequestRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType


class VaccineRequestService(private val vaccineRequestRepository: VaccineRequestRepository) {

    suspend fun getAllVaccineRequestByAnimalId(animalId: Int): List<VaccineRequestByAnimal> {
        return vaccineRequestRepository.getVaccineRequestsByAnimalId(animalId)
    }

    suspend fun getAllVaccineRequestFromVeterinary(): List<VaccineRequestByVeterinary> {
        return vaccineRequestRepository.getAllVaccineRequestsFromVeterinary()
    }

    suspend fun getVaccineRequestsFromVeterinaryById(vaccineRequestId: Int): VaccineRequestByVeterinary? {
        return vaccineRequestRepository.getVaccineRequestsFromVeterinaryById(vaccineRequestId)
    }

    suspend fun createVaccineRequest(animalId: Int): CreateVaccineRequestResponse? {
        val createVaccineRequest = CreateVaccineRequest(animalId)
        return vaccineRequestRepository.createVaccineRequest(createVaccineRequest)
    }

    suspend fun updateVaccineRequest(vaccineRequestId: Int, updatedData: UpdatedData): UpdateVaccineRequestResponse? {
        val updateVaccineRequest = UpdateVaccineRequest(
            updatedData = updatedData
        )
        return vaccineRequestRepository.updateVaccineRequest(vaccineRequestId, updateVaccineRequest)
    }
}
