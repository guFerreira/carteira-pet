package com.example.carteirapet.service

import com.example.carteirapet.repositories.CreateVaccineRequest
import com.example.carteirapet.repositories.CreateVaccineRequestResponse
import com.example.carteirapet.repositories.UpdateVaccineRequest
import com.example.carteirapet.repositories.UpdateVaccineRequestResponse
import com.example.carteirapet.repositories.UpdatedData
import com.example.carteirapet.repositories.VaccineRequestByAnimal
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.repositories.VaccineRequestRepository
import com.example.carteirapet.utils.DateUtils


class VaccineRequestService(private val vaccineRequestRepository: VaccineRequestRepository) {

    suspend fun getAllVaccineRequestByAnimalId(animalId: Int): List<VaccineRequestByAnimal> {
        val requests = vaccineRequestRepository.getVaccineRequestsByAnimalId(animalId)
        return requests.map { formatVaccineRequestByAnimalForDisplay(it) }
    }

    suspend fun getAllVaccineRequestFromVeterinary(): List<VaccineRequestByVeterinary> {
        val requests = vaccineRequestRepository.getAllVaccineRequestsFromVeterinary()
        return requests.map { formatVaccineRequestByVeterinaryForDisplay(it) }
    }

    suspend fun getVaccineRequestsFromVeterinaryById(vaccineRequestId: Int): VaccineRequestByVeterinary? {
        val request = vaccineRequestRepository.getVaccineRequestsFromVeterinaryById(vaccineRequestId)
        return request?.let { formatVaccineRequestByVeterinaryForDisplay(it) }
    }

    suspend fun createVaccineRequest(animalId: Int): CreateVaccineRequestResponse? {
        val createVaccineRequest = CreateVaccineRequest(animalId)
        return vaccineRequestRepository.createVaccineRequest(createVaccineRequest)
    }

    suspend fun updateVaccineRequest(vaccineRequestId: Int, updatedData: UpdatedData): UpdateVaccineRequestResponse? {
        val formattedApplicationDate = DateUtils.formatDateStringToRegister(updatedData.applicationDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formattedManufacturingDate = DateUtils.formatDateStringToRegister(updatedData.manufacturingDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formattedExpirationDate = DateUtils.formatDateStringToRegister(updatedData.expirationDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val formattedNextDoseDate = DateUtils.formatDateStringToRegister(updatedData.nextDoseDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        val updateVaccineRequest = UpdateVaccineRequest(
            updatedData = UpdatedData(
                applicationDate = formattedApplicationDate,
                applicationPlace = updatedData.applicationPlace,
                manufacturer = updatedData.manufacturer,
                batchCode = updatedData.batchCode,
                manufacturingDate = formattedManufacturingDate,
                expirationDate = formattedExpirationDate,
                nextDoseDate = formattedNextDoseDate,
                vaccineId = updatedData.vaccineId
            )
        )
        return vaccineRequestRepository.updateVaccineRequest(vaccineRequestId, updateVaccineRequest)
    }

    private fun formatVaccineRequestByVeterinaryForDisplay(vaccineRequest: VaccineRequestByVeterinary): VaccineRequestByVeterinary {
        return vaccineRequest.copy(
            applicationDate = vaccineRequest.applicationDate?.let { DateUtils.formatDateStringToShow(it) },
            manufacturingDate = vaccineRequest.manufacturingDate?.let { DateUtils.formatDateStringToShow(it) },
            expirationDate = vaccineRequest.expirationDate?.let { DateUtils.formatDateStringToShow(it) },
            nextDoseDate = vaccineRequest.nextDoseDate?.let { DateUtils.formatDateStringToShow(it) }
        )
    }

    private fun formatVaccineRequestByAnimalForDisplay(vaccineRequest: VaccineRequestByAnimal): VaccineRequestByAnimal {
        return vaccineRequest.copy(
            applicationDate = vaccineRequest.applicationDate?.let { DateUtils.formatDateStringToShow(it) },
            nextDoseDate = vaccineRequest.nextDoseDate?.let { DateUtils.formatDateStringToShow(it) }
        )
    }


}
