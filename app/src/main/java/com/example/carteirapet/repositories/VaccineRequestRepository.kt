package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class VaccineRequestByAnimal(
    val id: Int,
    val status: String,
    val vaccineName: String?,
    val applicationDate: String?,
    val batchCode: String?,
    val manufacturer: String?,
    val veterinaryDoctorName: String?,
    val crmv: String?,
    val nextDoseDate: String?,
    val storage: String?,
    val signedUrl: String?
)

@Serializable
data class VaccineRequestByVeterinary(
    val id: Int,
    val status: String,
    val vaccineName: String?,
    val applicationDate: String?,
    val batchCode: String?,
    val manufacturer: String?,
    val nextDoseDate: String?,
    val animalName: String?,
    val petGuardianName: String?,
    val storageUrl: String?,
    val signedUrl: String?
)

@Serializable
data class CreateVaccineRequest(
    val animalId: Int
)

@Serializable
data class CreateVaccineRequestResponse(
    val id: Int,
    val status: String
)

@Serializable
data class UpdateVaccineRequest(
    val updatedData: UpdatedData
)

@Serializable
data class UpdatedData(
    val applicationDate: String?,
    val applicationPlace: String?,
    val manufacturer: String?,
    val batchCode: String?,
    val manufacturingDate: String?,
    val expirationDate: String?,
    val nextDoseDate: String?,
    val vaccineId: Int?
)

@Serializable
data class UpdateVaccineRequestResponse(
    val signUrl: String?,
    val message: String?
)

class VaccineRequestRepository(private val client: HttpClient)  {

    suspend fun getVaccineRequestsByAnimalId(animalId: Int): List<VaccineRequestByAnimal>{
        val response: HttpResponse = client.get("http://35.239.21.191/vaccinerequest/animal/${animalId}") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<VaccineRequestByAnimal>>()
            return vaccines
        } else {
            emptyList()
        }
    }

    suspend fun getAllVaccineRequestsFromVeterinary(): List<VaccineRequestByVeterinary>{
        val response: HttpResponse = client.get("http://35.239.21.191/vaccinerequest/") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<VaccineRequestByVeterinary>>()
            return vaccines
        } else {
            emptyList()
        }
    }

    suspend fun createVaccineRequest(vaccineRequest: CreateVaccineRequest):CreateVaccineRequestResponse? {
        val response: HttpResponse = client.post("http://35.239.21.191/vaccinerequest/create") {
            contentType(ContentType.Application.Json)
            setBody(vaccineRequest)
        }
        return if (response.status == HttpStatusCode.Created) {
            return response.body<CreateVaccineRequestResponse>()
        } else {
            null
        }
    }

    suspend fun updateVaccineRequest(vaccineRequestId: Int, vaccineRequestInput: UpdateVaccineRequest):UpdateVaccineRequestResponse? {
        val response: HttpResponse = client.put("http://35.239.21.191/vaccinerequest/${vaccineRequestId}") {
            contentType(ContentType.Application.Json)
            setBody(vaccineRequestInput)
        }
        return if (response.status == HttpStatusCode.OK) {
            return response.body<UpdateVaccineRequestResponse>()
        } else {
            null
        }
    }
}