package com.example.carteirapet.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.carteirapet.R
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.LoginState
import com.example.carteirapet.viewModels.LoginViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(onSignUpClick: () -> Unit, onLoginSuccess: (screen: String) -> Unit, onRegisterProfileUserNavigate: () -> Unit, viewModel: LoginViewModel = koinViewModel()) {
    val loginState = viewModel.loginState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.carteirinha),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(180.dp)

            )
            Text(text = "Carteirinha", fontSize = 36.sp)
            Text(text = "O registro digital das vacinas do seu pet", fontSize = 12.sp)
        }

//        QRCodeScannerScreen()

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Senha") },
                visualTransformation = if (viewModel.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(imageVector = if (viewModel.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = if (viewModel.isPasswordVisible) "Hide password" else "Show password")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Button(
                onClick = { viewModel.login(onLoginSuccess, onRegisterProfileUserNavigate)},
                enabled = viewModel.isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedButton(
                onClick = { onSignUpClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Cor de fundo baseada no tema
                    contentColor = MaterialTheme.colorScheme.onSecondary // Cor do conteúdo (texto/ícone) baseada no tema
                )
            ) {
                Text("Criar conta")
            }
        }

        // Observa o estado do login e exibe a UI apropriada
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginState.Success -> {
                Text(text = (loginState as LoginState.Success).message)
            }
            is LoginState.Error -> {
                Text(text = (loginState as LoginState.Error).error, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}
//
//@Composable
//fun QRCodeScannerScreen() {
//    var scannedCode by remember { mutableStateOf<String?>(null) }
//    var hasCameraPermission by remember { mutableStateOf(false) }
//
//    val context = LocalContext.current
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        hasCameraPermission = isGranted
//    }
//
//    LaunchedEffect(Unit) {
//        permissionLauncher.launch(Manifest.permission.CAMERA)
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if (hasCameraPermission) {
//            QRCodeScanner { code ->
//                scannedCode = code
//            }
//        } else {
//            Text("Permissão de câmera negada", color = Color.Red, fontWeight = FontWeight.Bold)
//        }
//
//        scannedCode?.let {
//            Text("QR Code: $it", modifier = Modifier.padding(16.dp))
//        }
//    }
//}


//
//
//@Composable
//fun QRCodeScanner(onQrCodeScanned: (String) -> Unit) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val previewView = remember { PreviewView(context) }
//
//    AndroidView(
//        factory = { previewView },
//        modifier = Modifier.fillMaxSize()
//    ) { view ->
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = androidx.camera.core.Preview.Builder().build().also {
//                it.setSurfaceProvider(view.surfaceProvider)
//            }
//
//            val barcodeScanner = BarcodeScanning.getClient()
//
//            val imageAnalysis = ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build()
//
//            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
//                @Suppress("UnsafeOptInUsageError")
//                val mediaImage = imageProxy.image
//                if (mediaImage != null) {
//                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//                    barcodeScanner.process(image)
//                        .addOnSuccessListener { barcodes ->
//                            for (barcode in barcodes) {
//                                barcode.rawValue?.let {
//                                    onQrCodeScanned(it)
//                                }
//                            }
//                        }
//                        .addOnCompleteListener {
//                            imageProxy.close()
//                        }
//                }
//            }
//
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                lifecycleOwner, cameraSelector, preview, imageAnalysis
//            )
//        }, ContextCompat.getMainExecutor(context))
//    }
//}
//


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    CarteiraPetTheme {
        LoginScreen({}, {}, {})
    }
}

