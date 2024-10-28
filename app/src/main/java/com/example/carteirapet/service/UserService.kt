package com.example.carteirapet.service

import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.UserRepository


class UserService(private val userRepository: UserRepository) {
    suspend fun getUserInformations(): Profile? {
        return userRepository.getUserInformations()
    }

    suspend fun registerUserProfile(profile: Profile) {
        if(profile.isVet){
            userRepository.registerVeterinaryDoctor(profile)
        } else {
            userRepository.registerPetGuardian(profile)
        }
    }

    suspend fun updaterUserProfile(profile: Profile) {
        if(profile.isVet){
            userRepository.updateVeterinaryDoctor(profile)
        } else {
            userRepository.updatePetGuardian(profile)
        }
    }
}