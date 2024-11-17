package com.example.carteirapet.service

import com.example.carteirapet.repositories.Profile
import com.example.carteirapet.repositories.UserRegister
import com.example.carteirapet.repositories.UserRepository
import java.text.SimpleDateFormat
import java.util.Locale


class UserService(private val userRepository: UserRepository) {
    suspend fun getUserInformations(): Profile? {
        return userRepository.getUserInformations()
    }

    suspend fun checkUserRegister(): UserRegister? {
        return userRepository.checkUserRegister()
    }


    suspend fun registerUserProfile(profile: Profile) {
        if(profile.isVet == true){
            userRepository.registerVeterinaryDoctor(profile)
        } else {
            userRepository.registerPetGuardian(profile)
        }
    }

    suspend fun updaterUserProfile(profile: Profile) {
        if(profile.isVet == true){
            userRepository.updateVeterinaryDoctor(profile)
        } else {
            userRepository.updatePetGuardian(profile)
        }
    }
}