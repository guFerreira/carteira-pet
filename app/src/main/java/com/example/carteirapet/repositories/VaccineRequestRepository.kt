package com.example.carteirapet.repositories

import com.example.carteirapet.BuildConfig
import com.example.carteirapet.exceptions.VaccineRequestNotFoundException
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
data class VaccineRequestResponse(
    val id: Int,
    val status: String,
    val vaccineApplication: VaccineApplication? = null,
    val animalName: String,
    val animalSpecies: String,
    val veterinaryDoctorName: String? = null,
    val requestDate: String? = null,
    val expirationDate: String? = null,
    val acceptanceDate: String? = null,
    val storagedDocumentSignedUrl: String? = null,
    val signUrl: String? = null
)

@Serializable
data class VaccineApplication(
    val id: Int,
    val vaccine: Vaccine,
    val applicationDate: String,
    val applicationPlace: String,
    val batchCode: String,
    val manufacturer: String,
    val manufacturingDate: String,
    val expirationDate: String,
    val nextDoseDate: String?
)

@Serializable
data class VaccineRequestByAnimal(
    val id: Int,
    val status: String,
    val vaccineName: String? = null,
    val applicationDate: String? = null,
    val batchCode: String? = null,
    val manufacturer: String? = null,
    val veterinaryDoctorName: String? = null,
    val crmv: String? = null,
    val nextDoseDate: String? = null,
    val storage: String? = null,
    val signedUrl: String? = null
)

@Serializable
data class VaccineRequestByVeterinary(
    val id: Int,
    val status: String,
    val vaccine: Vaccine? = null,
    val applicationDate: String? = null,
    val applicationPlace: String? = null,
    val batchCode: String? = null,
    val manufacturer: String? = null,
    val manufacturingDate: String? = null,
    val expirationDate: String? = null,
    val nextDoseDate: String? = null,
    val animalName: String? = null,
    val animalSpecies: String? = null,
    val petGuardianName: String? = null,
    val storageUrl: String? = null,
    val signedUrl: String? = null
)

@Serializable
data class AcceptVaccineRequest(
    val vaccineRequestId: Int
)

@Serializable
data class RejectVaccineRequest(
    val vaccineRequestId: Int
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
    val applicationDate: String,
    val applicationPlace: String,
    val manufacturer: String,
    val batchCode: String,
    val manufacturingDate: String,
    val expirationDate: String,
    val nextDoseDate: String,
    val vaccineId: Int
)

@Serializable
data class UpdateVaccineRequestResponse(
    val signUrl: String? = null,
    val message: String? = null
)

class VaccineRequestRepository(private val client: HttpClient)  {
    private val url = BuildConfig.BASE_URL;

    suspend fun getActiveVaccineRequestByAnimalId(animalId: Int): VaccineRequestResponse{
        val response: HttpResponse = client.get("${url}/vaccinerequest/animal/${animalId}/active") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccineRequest = response.body<VaccineRequestResponse>()
            return vaccineRequest
        } else if (response.status == HttpStatusCode.NotFound) {
            throw VaccineRequestNotFoundException()
        } else {
            throw RuntimeException("Erro ao buscar a última solicitação de vacina ${animalId}")
        }
    }

    suspend fun getVaccineRequestsByAnimalId(animalId: Int): List<VaccineRequestResponse>{
        val response: HttpResponse = client.get("${url}/vaccinerequest/animal/${animalId}") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<VaccineRequestResponse>>()
            return vaccines
        } else {
            emptyList()
        }
    }

    suspend fun getAllVaccineRequestsFromVeterinary(): List<VaccineRequestResponse>{
        val response: HttpResponse = client.get("${url}/vaccinerequest") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<VaccineRequestResponse>>()
            return vaccines
        } else {
            emptyList()
        }
    }

    suspend fun getVaccineRequestsFromVeterinaryById(vaccineRequestId: Int): VaccineRequestResponse? {
        val response: HttpResponse = client.get("${url}/vaccinerequest/${vaccineRequestId}") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<VaccineRequestResponse>()
            return vaccines
        } else {
            null
        }
    }

    suspend fun createVaccineRequest(vaccineRequest: CreateVaccineRequest):VaccineRequestResponse {
        val response: HttpResponse = client.post("${url}/vaccinerequest/") {
            contentType(ContentType.Application.Json)
            setBody(vaccineRequest)
        }
        return if (response.status == HttpStatusCode.Created) {
            return response.body<VaccineRequestResponse>()
        } else {
            throw RuntimeException("Erro ao criar solicitação de vacina ${vaccineRequest.animalId}")
        }
    }

    suspend fun rejectVaccineRequest(rejectVaccineRequest: RejectVaccineRequest) {
        val response: HttpResponse = client.post("${url}/vaccinerequest/reject") {
            contentType(ContentType.Application.Json)
            setBody(rejectVaccineRequest)
        }
        return if (response.status == HttpStatusCode.OK) {
            return
        } else {
            throw RuntimeException("Erro ao aceitar solicitação de vacina de ID = ${rejectVaccineRequest.vaccineRequestId}")
        }
    }

    suspend fun acceptVaccineRequest(acceptVaccineRequest: AcceptVaccineRequest) {
        val response: HttpResponse = client.post("${url}/vaccinerequest/accept") {
            contentType(ContentType.Application.Json)
            setBody(acceptVaccineRequest)
        }
        return if (response.status == HttpStatusCode.Created) {
            return
        } else {
            throw RuntimeException("Erro ao aceitar solicitação de vacina de ID = ${acceptVaccineRequest.vaccineRequestId}")
        }
    }

    suspend fun updateVaccineRequest(vaccineRequestId: Int, vaccineRequestInput: UpdateVaccineRequest):UpdateVaccineRequestResponse? {
        val response: HttpResponse = client.put("${url}/vaccinerequest/${vaccineRequestId}") {
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