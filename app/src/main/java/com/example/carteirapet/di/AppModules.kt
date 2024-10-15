package com.example.carteirapet.di


import com.example.carteirapet.repositories.AuthRepository
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.TokenManagerService
import com.example.carteirapet.viewModels.LoginViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
    singleOf(::AuthService)
    viewModelOf(::LoginViewModel)
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
        }
    }
}