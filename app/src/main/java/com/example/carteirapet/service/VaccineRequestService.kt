package com.example.carteirapet.service

import com.example.carteirapet.repositories.AcceptVaccineRequest
import com.example.carteirapet.repositories.CreateVaccineRequest
import com.example.carteirapet.repositories.RejectVaccineRequest
import com.example.carteirapet.repositories.UpdateVaccineRequest
import com.example.carteirapet.repositories.UpdateVaccineRequestResponse
import com.example.carteirapet.repositories.UpdatedData
import com.example.carteirapet.repositories.VaccineRequestByAnimal
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.repositories.VaccineRequestRepository
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.utils.DateUtils


class VaccineRequestService(private val vaccineRequestRepository: VaccineRequestRepository) {

    suspend fun getActiveVaccineRequestByAnimalId(animalId: Int): VaccineRequestResponse {
        return vaccineRequestRepository.getActiveVaccineRequestByAnimalId(animalId)
    }


    suspend fun getAllVaccineRequestByAnimalId(animalId: Int): List<VaccineRequestResponse> {
        return vaccineRequestRepository.getVaccineRequestsByAnimalId(animalId)
    }

    suspend fun getAllVaccineRequestFromVeterinary(): List<VaccineRequestResponse> {
        return vaccineRequestRepository.getAllVaccineRequestsFromVeterinary()
    }

    suspend fun getVaccineRequestsFromVeterinaryById(vaccineRequestId: Int): VaccineRequestResponse? {
        return vaccineRequestRepository.getVaccineRequestsFromVeterinaryById(vaccineRequestId)
    }

    suspend fun createVaccineRequest(animalId: Int): VaccineRequestResponse {
        val createVaccineRequest = CreateVaccineRequest(animalId)
        return vaccineRequestRepository.createVaccineRequest(createVaccineRequest)
    }

    suspend fun rejectVaccineRequest(vaccineRequestId: Int) {
        val rejectVaccineRequest = RejectVaccineRequest(vaccineRequestId)
        vaccineRequestRepository.rejectVaccineRequest(rejectVaccineRequest)
    }

    suspend fun acceptVaccineRequest(vaccineRequestId: Int) {
        val acceptVaccineRequest = AcceptVaccineRequest(vaccineRequestId)
        vaccineRequestRepository.acceptVaccineRequest(acceptVaccineRequest)
    }

    suspend fun updateVaccineRequest(vaccineRequestId: Int, updatedData: UpdatedData): UpdateVaccineRequestResponse? {
//        val formattedApplicationDate = DateUtils.formatDateStringToRegister(updatedData.applicationDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        val formattedManufacturingDate = DateUtils.formatDateStringToRegister(updatedData.manufacturingDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        val formattedExpirationDate = DateUtils.formatDateStringToRegister(updatedData.expirationDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//        val formattedNextDoseDate = DateUtils.formatDateStringToRegister(updatedData.nextDoseDate, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//

        val formattedApplicationDate = updatedData.applicationDate
        val formattedManufacturingDate = updatedData.manufacturingDate
        val formattedExpirationDate = updatedData.expirationDate
        val formattedNextDoseDate = updatedData.nextDoseDate


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

//    private fun formatVaccineRequestByAnimalForDisplay(vaccineRequest: VaccineRequestResponse): VaccineRequestResponse {
//        return vaccineRequest.copy(
//            applicationDate = vaccineRequest.applicationDate?.let { DateUtils.formatDateStringToShow(it) },
//            nextDoseDate = vaccineRequest.nextDoseDate?.let { DateUtils.formatDateStringToShow(it) }
//        )
//    }


}
