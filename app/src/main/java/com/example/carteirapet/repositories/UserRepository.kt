package com.example.carteirapet.repositories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
data class UserResponse(
    val id: Int,
    val petGuardian: PetGuardian?,
    val veterinaryDoctor: VeterinaryDoctor? // Pode ser nulo
)
@Serializable
data class PetGuardian(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val cpf: String,
    val userId: Int,
    val createdAt: String,
    val updatedAt: String,
    val addressId: Int
)
@Serializable
data class VeterinaryDoctor(
    val id: Int,
    val firstName: String,
    val lastName: String,)

@Serializable
data class Profile(
    val isVet: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val cpf: String,
    val address: Address
)
@Serializable
data class Address(
    val cep: Int,
    val street: String,
    val number: Int,
    val complement: String,
    val neighborhood: String,
    val city: String,
    val state: String,
)

class UserRepository(private val client: HttpClient) {
    suspend fun getUserInformations(): UserResponse? {
        val response: HttpResponse = client.get("http://35.239.21.191/users") {
            contentType(ContentType.Application.Json)
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val userResponse = response.body<UserResponse>()
            return userResponse
        } else {
            null
        }
    }

    suspend fun registerPetGuardian(profile: Profile): PetGuardian? {
        val response: HttpResponse = client.post("http://35.239.21.191/petguardian/register") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.Created) {
            response.body<PetGuardian>()
        } else {
            throw Exception("Erro ao realizar cadastro")
        }
    }

    suspend fun updatePetGuardian(profile: Profile): PetGuardian? {
        val response: HttpResponse = client.put("http://35.239.21.191/petguardian/update") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.OK) {
            response.body<PetGuardian>()
        } else {
            throw Exception("Erro ao atualizar perfil do tutor")
        }
    }

    suspend fun registerVeterinaryDoctor(profile: Profile): VeterinaryDoctor? {
        val response: HttpResponse = client.post("http://35.239.21.191/veterinary/register") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.Created) {
            response.body<VeterinaryDoctor>()
        } else {
            throw Exception("Erro ao realizar cadastro")
        }
    }

    suspend fun updateVeterinaryDoctor(profile: Profile): VeterinaryDoctor? {
        val response: HttpResponse = client.put("http://35.239.21.191/veterinary/update") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.OK) {
            response.body<VeterinaryDoctor>()
        } else {
            throw Exception("Erro ao atualizar perfil do médico veterinário")
        }
    }
}