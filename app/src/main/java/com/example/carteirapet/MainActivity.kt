package com.example.carteirapet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.example.carteirapet.screen.MyPetsScreen
import com.example.carteirapet.ui.theme.CarteiraPetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()

//            NavHost(navController = navController, startDestination = "paginaExemplo") {
//                composable("paginaExemplo") {
//                    Column {
//                        Text(text = "deu bom?")
//                    }
//                }
//            }
            CarteiraPetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

//                    if (intent.action == Intent.ACTION_VIEW) {
//                        HandleQrCodeUri(intent.data, navController)
//                    }else{
                        MooApp(navController)

//                    }

//                    MyPetsScreen()
                }
            }
        }
    }
}

@Composable
fun HandleQrCodeUri(uri: Uri?, navController: NavHostController) {
    if (uri != null && uri.scheme == "meupetscheme") {
        when (uri.host) {
            "paginaexemplo" -> {
                navController.navigate("paginaExemplo")
            }
            // Outros hosts e ações
        }
    }
}