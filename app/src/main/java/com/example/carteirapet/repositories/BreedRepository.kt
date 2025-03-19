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
data class Breed(
    val id: Int?,
    val name: String
)

class BreedRepository(private val client: HttpClient) {
    private val url = "10.0.2.2:3000";
    suspend fun getBreeds(specie: String): List<Breed> {
        val response: HttpResponse = client.get("http://${url}/breeds") {
            contentType(ContentType.Application.Json)
            parameter("specie", specie)
        }

        return if (response.status == HttpStatusCode.OK) {
            val breeds = response.body<List<Breed>>()
            return breeds
        } else {
            return emptyList()
        }
    }

}