package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class Vaccine(
    val id: Int,
    val name: String
)

class VaccineRepository(private val client: HttpClient)  {
    suspend fun getAllVaccinesBySpecies(species: String): List<Vaccine>{
        val response: HttpResponse = client.get("http://35.239.21.191/vaccine") {
            contentType(ContentType.Application.Json)
            parameter("species", species)
        }
        return if (response.status == HttpStatusCode.OK) {
            val vaccines = response.body<List<Vaccine>>()
            return vaccines
        } else {
            emptyList()
        }
    }
}