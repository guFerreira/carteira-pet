package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime



@Serializable
data class Animal(
    val id: Int,
    val name: String,
    val microchip: String?, // Microchip é opcional
    val species: String,
    val sex: String,
    val neutered: Boolean,
    var birthDate: String,
    val weight: Float,
    val conditions: String?, // Condições preexistentes
    val breeds: List<Breed>
)



class AnimalRepository(private val client: HttpClient) {
    suspend fun getAnimals(): List<Animal>{
        val response: HttpResponse = client.get("http://35.239.21.191/animal") {
            contentType(ContentType.Application.Json)
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val pets = response.body<List<Animal>>()
            return pets
        } else {
            emptyList()
        }
    }

    suspend fun getAnimalById(animalId: Int): Animal? {
        val response: HttpResponse = client.get("http://35.239.21.191/animal/$animalId") {
            contentType(ContentType.Application.Json)
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val pets = response.body<Animal>()
            return pets
        } else {
            null
        }
    }

    suspend fun registerAnimal(animal: Animal): Animal?{
        val response: HttpResponse = client.post("http://35.239.21.191/animal/register") {
            contentType(ContentType.Application.Json)
            setBody(animal)
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.Created) {
            val animalResponse = response.body<Animal>()
            return animalResponse
        } else {
            null
        }
    }
}