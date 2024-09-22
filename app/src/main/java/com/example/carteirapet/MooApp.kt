package com.example.carteirapet

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.carteirapet.screen.EditUserProfileScreen
import com.example.carteirapet.screen.LoginScreen
import com.example.carteirapet.screen.MyPetsScreen
import com.example.carteirapet.screen.PetInformation
import com.example.carteirapet.screen.RegisterPetScreen
import com.example.carteirapet.screen.RegisterProfileUserScreen
import com.example.carteirapet.screen.RegisterVaccineScreen
import com.example.carteirapet.screen.SignupScreen

@Composable
fun MooApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("home") {
            MyPetsScreen(
                goToRegisterPetScreen = { navController.navigate("registerPet") },
                goToEditUserProfileScreen = { navController.navigate("editUserProfileInfos") },
                goToLoginScreen = { navController.navigate("login") },
                goToPetInformation = { navController.navigate("vaccinationCard") }
            )
        }

        composable("registerPet") {
            RegisterPetScreen(backToHomeScreen = { navController.popBackStack() })
        }
        composable("vaccinationCard") {
            PetInformation(
                goToHomeScreen = { navController.popBackStack() },
                goRegisterVaccineScreen = { navController.navigate("registerVaccine") })
        }

        composable("registerVaccine") {
            RegisterVaccineScreen(goToVaccineCardScreen = { navController.popBackStack() })

        }
        composable("signup") {
            SignupScreen(
                goToLoginScreen = { navController.popBackStack() }
            )
        }
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = { navController.navigate("registerUserProfileInfos") })
        }

        composable("registerUserProfileInfos") {
            RegisterProfileUserScreen(
                goToLoginScreen = { navController.navigate("login") },
                goToHomeScreen = { navController.navigate("home") }
            )
        }

        composable("editUserProfileInfos") {
            EditUserProfileScreen(
                goToHomeScreen = { navController.navigate("home") }
            )
        }

    }
}
