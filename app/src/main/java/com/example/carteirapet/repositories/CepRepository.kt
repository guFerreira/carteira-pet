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
data class Cep(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val unidade: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
    val estado: String,
    val regiao: String,
    val ibge: String,
    val gia: String,
    val ddd: String,
    val siafi: String
)


class CepRepository(private val client: HttpClient) {
    private val url = "https://viacep.com.br/ws/";
    suspend fun getCep(cep: String): Cep {
        val response: HttpResponse = client.get("${url}/${cep}/json/") {
            contentType(ContentType.Application.Json)
        }

        return if (response.status == HttpStatusCode.OK) {
            val cep = response.body<Cep>()
            return cep
        } else {
            throw Exception("Erro ao buscar CEP")
        }
    }

}