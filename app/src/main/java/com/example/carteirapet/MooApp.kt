package com.example.carteirapet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.carteirapet.screen.EditUserProfileScreen
import com.example.carteirapet.screen.LoginScreen
import com.example.carteirapet.screen.MyPetsScreen
import com.example.carteirapet.screen.PetInformation
import com.example.carteirapet.screen.RegisterPetScreen
import com.example.carteirapet.screen.RegisterProfileUserScreen
import com.example.carteirapet.screen.RegisterVaccineScreen
import com.example.carteirapet.screen.SignupScreen
import com.example.carteirapet.service.AuthService
import com.example.carteirapet.service.UserService

@Composable
fun MooApp(navController: NavHostController, authService: AuthService, userService: UserService) {
    var isLoggedIn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLoggedIn = authService.getAccessToken() != null && authService.getRefreshToken() != null
        if (isLoggedIn) {
            var userProfile = userService.getUserInformations()
            if (userProfile != null) {
                if (!userProfile.isRegistered) {
                    navController.navigate("registerUserProfileInfos")
                }
            }
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("home") {
            MyPetsScreen(
                goToRegisterPetScreen = { navController.navigate("registerPet") },
                goToEditUserProfileScreen = { navController.navigate("editUserProfileInfos") },
                goToLoginScreen = { navController.navigate("login") },
                goToPetInformation = { petId ->
                    navController.navigate("vaccinationCard/$petId")
                }
            )
        }

        composable("registerPet") {
            RegisterPetScreen(backToHomeScreen = { navController.popBackStack() })
        }
        composable(
            "vaccinationCard/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType })
        ) {
            val petId = it.arguments?.getInt("animalId")
            PetInformation(
                petId = petId,
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
                onLoginSuccess = { navController.navigate("home") },
                onRegisterProfileUserNavigate = { navController.navigate("registerUserProfileInfos") })
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
