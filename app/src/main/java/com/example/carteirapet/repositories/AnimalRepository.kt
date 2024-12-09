package com.example.carteirapet.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime



@Serializable
data class Animal(
    val id: Int,
    val name: String,
    val photo: String? = null,
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

    suspend fun registerAnimal(animal: Animal, imageBytes: ByteArray?): Animal? {
        val multipartData = formData {
            // Adiciona cada campo do objeto `animal` individualmente
            append("name", animal.name)
            append("microchip", animal.microchip ?: "")
            append("species", animal.species)
            append("sex", animal.sex)
            append("neutered", animal.neutered.toString())
            append("birthDate", animal.birthDate)
            append("weight", animal.weight.toString())
            append("conditions", animal.conditions ?: "")

            // Serializa `breeds` para um formato aceitável pelo backend
            append("breeds", Json.encodeToString(animal.breeds))

            // Adiciona a imagem, se disponível
            if (imageBytes != null) {
                append("file", imageBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=\"photo.jpg\"")
                })
            }
        }

        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "http://35.239.21.191/animal",
            formData = multipartData
        )

        return if (response.status == HttpStatusCode.Created) {
            response.body<Animal>()
        } else {
            throw Exception("Erro ao criar animal: ${response.status}")
        }
    }


}