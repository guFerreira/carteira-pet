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
import androidx.compose.foundation.layout.safeContentPadding
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
import androidx.compose.material3.TextButton
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
fun LoginScreen(
    onSignUpClick: () -> Unit,
    onLoginSuccess: (screen: String) -> Unit,
    onRegisterProfileUserNavigate: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val loginState = viewModel.loginState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp) // Padding horizontal um pouco maior para respiro
            .safeContentPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Seção do Logo e Títulos
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp) // Espaçamento maior para separar do formulário
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_grande),
                contentDescription = "Logo do aplicativo Carteirinha", // Descrição mais específica
                modifier = Modifier
                    .size(180.dp) // Tamanho fixo para o logo, controlando o preenchimento da largura
                    .padding(bottom = 16.dp) // Espaçamento entre o logo e o título
            )
            Text(
                text = "Carteirinha",
                style = MaterialTheme.typography.displaySmall, // Título principal (h1 ou h2 no M3)
                color = MaterialTheme.colorScheme.primary // Usando a cor primária do tema
            )
            Spacer(modifier = Modifier.height(4.dp)) // Pequeno espaçamento
            Text(
                text = "O registro digital das vacinas do seu pet",
                style = MaterialTheme.typography.bodyLarge, // Subtítulo com texto de corpo maior
                color = MaterialTheme.colorScheme.onSurfaceVariant // Cor para textos secundários
            )
        }

        // Campos de Formulário
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Usuário") },
                singleLine = true, // Campos de texto de linha única para formulários
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento consistente entre campos

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
                        Icon(
                            imageVector = if (viewModel.isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (viewModel.isPasswordVisible) "Esconder senha" else "Mostrar senha" // Descrições mais claras
                        )
                    }
                },
                singleLine = true, // Campos de texto de linha única
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Botões de Ação
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp) // Aumenta o espaçamento antes dos botões
        ) {
            Button(
                onClick = { viewModel.login(onLoginSuccess, onRegisterProfileUserNavigate) },
                enabled = viewModel.isLoginEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Altura padrão do M3 para botões de ação
            ) {
                Text("Entrar", style = MaterialTheme.typography.titleMedium) // Estilo de texto do botão
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento consistente entre botões

            // Usando um TextButton ou OutlinedButton para "Criar conta"
            // Elevated Button é uma opção, mas TextButton ou OutlinedButton
            // podem ser mais adequados para ações secundárias ou de navegação para fora do fluxo principal.
            // Vou sugerir TextButton para uma aparência mais leve para "Criar conta".
            TextButton( // Alterado de ElevatedButton para TextButton
                onClick = { onSignUpClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Criar conta", style = MaterialTheme.typography.titleMedium)
            }
        }

        // Observa o estado do login e exibe a UI apropriada
        // Colocando os indicadores de estado abaixo dos botões e com espaçamento
        Spacer(modifier = Modifier.height(24.dp)) // Espaçamento antes dos indicadores de estado

        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.size(48.dp)) // Tamanho padrão para CircularProgressIndicator
            }
            is LoginState.Success -> {
                Text(
                    text = loginState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary // Cor primária para sucesso
                )
            }
            is LoginState.Error -> {
                Text(
                    text = loginState.error,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error // Cor de erro do tema
                )
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

