package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class Vaccine(
    val id: Int,
    val name: String,
    val status: String,
    val applicationDate: String,
    val batchCode: String,
    val manufacturer: String,
    val veterinaryDoctorName: String,
    val crmv: String,
    val signedPdf: String
)

class VaccineRepository(private val client: HttpClient)  {
    suspend fun getVaccineRequestByAnimalId(animalId: Int): List<Vaccine>{
        val response: HttpResponse = client.get("http://35.239.21.191/vaccinerequest/animal/${animalId}") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<Vaccine>>()
            return vaccines
        } else {
            emptyList()
        }
    }
}