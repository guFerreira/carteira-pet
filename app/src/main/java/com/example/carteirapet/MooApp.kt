package com.example.carteirapet

import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.navDeepLink
import com.example.carteirapet.exceptions.UnauthorizedException
import com.example.carteirapet.screen.CreateVaccineRequestFormScreen
import com.example.carteirapet.screen.CreateVaccineRequestScreen
import com.example.carteirapet.screen.EditUserProfileScreen
import com.example.carteirapet.screen.LoginScreen
import com.example.carteirapet.screen.MyPetsScreen
import com.example.carteirapet.screen.PetInformation
import com.example.carteirapet.screen.RegisterPetScreen
import com.example.carteirapet.screen.RegisterProfileUserScreen
import com.example.carteirapet.screen.RegisterVaccineScreen
import com.example.carteirapet.screen.SignupScreen
import com.example.carteirapet.screen.VeterinaryHomeScreen
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

                // Verifica se foi aberto via Deep Link
                val deepLinkEntry = navController.currentBackStackEntry?.arguments?.get("animalId")
                if (deepLinkEntry != null) {
                    return@launch
                }

                val hasTokenSaved = authService.getAccessToken() != null && authService.getRefreshToken() != null
                if (!hasTokenSaved) {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                } else {
                    val user = userService.checkUserRegister()

                    if (user == null || user.isRegistered) {
                        val profile = userService.getUserInformations()
                        if (profile != null) {
                            if(profile.isVet){
                                navController.navigate("homeVeterinary") {
                                    popUpTo("login") { inclusive = true }
                                }
                                return@launch
                            } else {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                                return@launch
                            }
                        }
                    } else {
                        navController.navigate("registerUserProfileInfos") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        return@launch
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
        //telas do veterinário

        composable("homeVeterinary") {
            VeterinaryHomeScreen(
                goToEditVeterinaryScreen = { navController.navigate("editUserProfileInfos") },
                goToLoginScreen = { navController.navigate("login") },
                goToUpdateVaccineRequestScreen = { vaccineRequestId: Int -> navController.navigate("createVaccineRequestForm/${vaccineRequestId}") }
            )
        }

        // rota para a tela de criação de vacina escaneada pelo QR code
        composable(
            "createVaccineRequest/{animalId}",
            arguments = listOf(navArgument("animalId") { type = NavType.IntType }),
            deepLinks = listOf(navDeepLink { uriPattern = "app://moo/createVaccineRequest/{animalId}" })
        ) { backStackEntry ->
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            // Recupera o ID do animal da rota
            val petId = backStackEntry.arguments?.getInt("animalId")

            // Estado para gerenciar se a tela pode ser acessada
            var canAccess by remember { mutableStateOf<Boolean?>(null) }

            // Recupera os serviços de autenticação
            val isAuthenticated = authService.getAccessToken() != null

            LaunchedEffect(isAuthenticated) {
                if (isAuthenticated) {
                    coroutineScope.launch {
                        val user = userService.getUserInformations()
                        canAccess = user?.isVet == true // Permite acesso apenas se for veterinário
                    }
                } else {
                    canAccess = false // Não autenticado
                }
            }

            // Tratamento de estados
            when (canAccess) {
                true -> {
                    // Renderiza a tela normalmente
                    CreateVaccineRequestScreen(petId = petId, {
                        navController.navigate("homeVeterinary") {
                            popUpTo("createVaccineRequest") { inclusive = true }
                        }
                    }, goToVaccineRequestFormScreen = { vaccineRequestId ->
                        navController.navigate("createVaccineRequestForm/$vaccineRequestId")
                    })
                }
                false -> {
                    // Redireciona para a tela de login ou inicial com base no contexto
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Acesso negado. Realize login ou verifique suas permissões.", Toast.LENGTH_SHORT).show()
                        if (!isAuthenticated) {
                            navController.navigate("login") {
                                popUpTo("createVaccineRequest") { inclusive = true }
                            }
                        } else {
                            navController.navigate("home") {
                                popUpTo("createVaccineRequest") { inclusive = true }
                            }
                        }
                    }
                }
                null -> {
                    // Exibe um estado de carregamento enquanto verifica as condições
                    CircularProgressIndicator()
                }
            }
        }

        // rota para dar o update dos dados da solicitacao de vacina criada anteriormente pelo qrcode
        composable("createVaccineRequestForm/{vaccineRequestId}", arguments = listOf(navArgument("vaccineRequestId") { type = NavType.IntType })) {
            val vaccineRequestId = it.arguments?.getInt("vaccineRequestId")
            CreateVaccineRequestFormScreen(vaccineRequestId = vaccineRequestId, goToHomeScreen = { navController.navigate("homeVeterinary") })
        }

        //telas do tutor
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
                goRegisterVaccineScreen = { navController.navigate("registerVaccine/$petId") })
        }


        composable("registerVaccine/{animalId}", arguments = listOf(navArgument("animalId") { type = NavType.IntType })) {
            val petId = it.arguments?.getInt("animalId")
            RegisterVaccineScreen(petId = petId, goToVaccineCardScreen = { navController.popBackStack() })
        }

        composable("editUserProfileInfos") {
            EditUserProfileScreen(
                goToHomeScreen = { userType ->
                    if (userType == 1) {
                        navController.navigate("homeVeterinary")
                    } else{
                        navController.navigate("home")
                    }
                }
            )
        }

        //rota dos dois tipos de usuario
        composable("signup") {
            SignupScreen(
                goToLoginScreen = { navController.popBackStack() }
            )
        }
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = { screenName: String -> navController.navigate(screenName) },
                onRegisterProfileUserNavigate = { navController.navigate("registerUserProfileInfos") })
        }

        composable("registerUserProfileInfos") {
            RegisterProfileUserScreen(
                goToLoginScreen = { navController.navigate("login") },
                goToHomeScreen = { userType ->
                    if (userType == 1){
                        navController.navigate("homeVeterinary")
                    }else {
                        navController.navigate("home")
                    }
                }
            )
        }

    }
}
