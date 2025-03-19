package com.example.carteirapet.repositories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.carteirapet.exceptions.UnauthorizedException
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
    val isVet: Boolean,
    val isRegistered: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val cpf: String,
    val crmv: String,
    val address: Address
)

@Serializable
data class UserRegister(
    val isRegistered: Boolean,
)

@Serializable
data class Profile(
    val isVet: Boolean,
    val isRegistered: Boolean,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val cpf: String,
    val address: Address,
    val crmv: String? = null
)

@Serializable
data class ProfileCreateResponse(
    val isVet: Boolean,
    val crmv: String?,
    val isRegistered: Boolean,
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
    val city: String,
    val state: String,
)

class UserRepository(private val client: HttpClient) {
    private val url = "10.0.2.2:3000";
    suspend fun getUserInformations(): Profile? {
        val response: HttpResponse = client.get("http://${url}/users") {
            contentType(ContentType.Application.Json)
        }

        // Verifica se o login foi bem-sucedido e retorna o token JWT
        return if (response.status == HttpStatusCode.OK) {
            val userResponse = response.body<Profile>()
            return userResponse
        } else {
            null
        }
    }

    suspend fun checkUserRegister(): UserRegister {
        val response: HttpResponse = client.get("http://${url}/users/checkRegister") {
            contentType(ContentType.Application.Json)
        }
        return if (response.status == HttpStatusCode.OK) {
            val userResponse = response.body<UserRegister>()
            return userResponse
        } else {
            return UserRegister(isRegistered = false)
        }
    }

    suspend fun registerPetGuardian(profile: ProfileCreateResponse): Boolean {
        val response: HttpResponse = client.post("http://${url}/petguardian") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.Created) {
            return true
        } else {
            return false
        }
    }

    suspend fun updatePetGuardian(profile: ProfileCreateResponse): Boolean {
        val response: HttpResponse = client.put("http://${url}/petguardian") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.OK) {
            true
        } else {
            throw Exception("Erro ao atualizar perfil do tutor")
        }
    }

    suspend fun registerVeterinaryDoctor(profile: ProfileCreateResponse): Boolean {
        val response: HttpResponse = client.post("http://${url}/veterinary") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.Created) {
            return true
        } else {
            return false
        }
    }

    suspend fun updateVeterinaryDoctor(profile: ProfileCreateResponse): Boolean {
        val response: HttpResponse = client.put("http://${url}/veterinary") {
            contentType(ContentType.Application.Json)
            setBody(profile)
        }
        return if (response.status == HttpStatusCode.OK) {
            true
        } else {
            throw Exception("Erro ao atualizar perfil do médico veterinário")
        }
    }
}