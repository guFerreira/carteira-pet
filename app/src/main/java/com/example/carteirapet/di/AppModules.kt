package com.example.carteirapet.di


import com.example.carteirapet.exceptions.UnauthorizedException
import com.example.carteirapet.repositories.AnimalRepository
import com.example.carteirapet.repositories.AuthRepository
import com.example.carteirapet.repositories.BreedRepository
import com.example.carteirapet.repositories.UserRepository
import com.example.carteirapet.repositories.VaccineRepository
import com.example.carteirapet.service.AnimalService
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.BreedService
import com.example.carteirapet.service.TokenManagerService
import com.example.carteirapet.service.UserService
import com.example.carteirapet.service.VaccineService
import com.example.carteirapet.viewModels.EditUserProfileViewModel
import com.example.carteirapet.viewModels.LoginViewModel
import com.example.carteirapet.viewModels.MyPetsViewModel
import com.example.carteirapet.viewModels.PetInformationViewModel
import com.example.carteirapet.viewModels.RegisterPetViewModel
import com.example.carteirapet.viewModels.RegisterProfileUserViewModel
import com.example.carteirapet.viewModels.SignupViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val appModule = module {
    singleOf(::TokenManagerService)

    singleOf(::AuthRepository)
    singleOf(::UserRepository)
    singleOf(::AnimalRepository)
    singleOf(::VaccineRepository)
    singleOf(::BreedRepository)

    singleOf(::AuthService)
    singleOf(::UserService)
    singleOf(::AnimalService)
    singleOf(::VaccineService)
    singleOf(::BreedService)

    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::RegisterProfileUserViewModel)
    viewModelOf(::MyPetsViewModel)
    viewModelOf(::RegisterPetViewModel)
    viewModelOf(::EditUserProfileViewModel)
    viewModelOf(::PetInformationViewModel)
}

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                // Instala o plugin de serialização para JSON
                json()
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenManagerService = get<TokenManagerService>()
                        val accessToken = tokenManagerService.getAccessToken()
                        val refreshToken = tokenManagerService.getRefreshToken()

                        // Verifica se ambos os tokens estão disponíveis
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            null // Retorna null se os tokens não estiverem disponíveis
                        }
                    }
                    refreshTokens {
                        val tokenManagerService = get<TokenManagerService>()
                        val authRepository = get<AuthRepository>()

                        val refreshToken = tokenManagerService.getRefreshToken()
                        if (refreshToken == null) {
                            throw UnauthorizedException()
                        }
                        // Tenta fazer o refresh do token usando o token de atualização
                        val newTokens = authRepository.refreshAccessToken(refreshToken)

                        // Se a atualização for bem-sucedida, salva e retorna os novos tokens
                        if (newTokens != null) {
                            tokenManagerService.saveTokens(newTokens.accessToken, refreshToken)
                            BearerTokens(newTokens.accessToken, refreshToken)
                        } else {
                            throw UnauthorizedException()
                        }
                    }
                }
            }
        }
    }
}