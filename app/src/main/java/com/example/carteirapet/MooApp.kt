package com.example.carteirapet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.carteirapet.screen.LoginScreen
import com.example.carteirapet.screen.MyPetsScreen
import com.example.carteirapet.screen.PetInformation
import com.example.carteirapet.screen.PetInformationScreen
import com.example.carteirapet.screen.RegisterPetScreen
import com.example.carteirapet.screen.SignupScreen

@Composable
fun MooApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("home") {
            MyPetsScreen(goToRegisterPetScreen = { navController.navigate("registerPet") })
        }
        composable("registerPet"){
            RegisterPetScreen(backToHomeScreen = { navController.popBackStack() })
        }
        composable("vaccinationCard") {
            PetInformation()
        }
        composable("signup") {
            SignupScreen(
                goToLoginScreen = { navController.popBackStack() }
            )
        }
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("signup") },
                onLoginSuccess = { navController.navigate("home") })
        }

    }
}
