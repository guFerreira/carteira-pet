package com.example.carteirapet.service

import com.example.carteirapet.repositories.AuthRepository
import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.UserRepository
import com.example.carteirapet.repositories.UserResponse



class UserService(private val userRepository: UserRepository) {
    suspend fun getUserInformations(): UserResponse? {
        return userRepository.getUserInformations()
    }

    suspend fun registerUserProfile(profile: Profile) {
        if(profile.isVet){
            userRepository.registerVeterinaryDoctor(profile)
        } else {
            userRepository.registerPetGuardian(profile)
        }
    }
}