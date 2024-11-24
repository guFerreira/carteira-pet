package com.example.carteirapet

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.carteirapet.exceptions.UnauthorizedException
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
import kotlinx.coroutines.launch


@Composable
fun MooApp(navController: NavHostController, authService: AuthService, userService: UserService) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val hasTokenSaved = authService.getAccessToken() != null && authService.getRefreshToken() != null
                if (!hasTokenSaved) {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                } else {
                    val user = userService.checkUserRegister()

                    if (user == null || user.isRegistered) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("registerUserProfileInfos")
                    }
                }

            } catch (e: UnauthorizedException) {
                // Captura a exceção e navega para a tela de login
                Toast.makeText(context, "Token de refresh expirado. Faça login novamente.", Toast.LENGTH_LONG).show()
                navController.navigate("login")
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
