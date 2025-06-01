package com.example.carteirapet.screen

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.ButtonOpenLinkForDigitalSignatureOnBrowser
import com.example.carteirapet.screen.components.CardUser
import com.example.carteirapet.screen.components.Logo
import com.example.carteirapet.screen.components.NextApplicationDate
import com.example.carteirapet.screen.components.PetInfoRow
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.screen.components.StatusIndicator
import com.example.carteirapet.screen.components.VaccineActions
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.VaccineStatus
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.utils.DateUtils
import com.example.carteirapet.viewModels.VeterinaryHomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinaryHomeScreen(
    goToLoginScreen: () -> Unit,
    goToEditVeterinaryScreen: () -> Unit,
    goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit,
    viewModel: VeterinaryHomeViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadVaccineRequestsFromVeterinary(onError = { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Logo()
                },
                actions = {
                    IconButton(onClick = {goToEditVeterinaryScreen()}) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Perfil do usuário",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = {
                        viewModel.logout(
                            goToLoginScreen,
                            onError = { message ->
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "Sair",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {},
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Registrar Nova vacina") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CardUser("Médico veterinário", true)

//                QRCodeScannerScreen()

                if (viewModel.isLoading && viewModel.vaccines.isEmpty()){
                    Row (
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        CircularProgressIndicator()
                    }
                } else {
                    PullToRefreshBox(isRefreshing = viewModel.isLoading, onRefresh = {
                        viewModel.loadVaccineRequestsFromVeterinary { message ->
                            Toast.makeText(
                                context, message, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        VaccineRequests(viewModel.vaccines, goToUpdateVaccineRequestScreen)
                    }
               }
            }
        }
    }
}

@Composable
fun VaccineRequests(vaccines: List<VaccineRequestResponse>, goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit) {
    if (vaccines.isEmpty()){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Nenhuma vacina registrada por você \uD83D\uDC89")
        }
    } else{
        LazyColumn() {
            items(vaccines.size) { item ->
                VaccineVeterinaryItem(vaccines[item], goToUpdateVaccineRequestScreen, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun VaccineVeterinaryItem(
    vaccineRequest: VaccineRequestResponse,
    goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                if (vaccineRequest.vaccineApplication?.vaccine?.name != null) {
                    Text(
                        text = vaccineRequest.vaccineApplication?.vaccine?.name ?: "Nome da vacina",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = "O registro de vacina não foi concluído",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                }


                vaccineRequest.status?.let { status ->
                    StatusIndicator(status = status.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Data de aplicação: ${vaccineRequest.vaccineApplication?.applicationDate?.let {
                        DateUtils.formatDateStringToShow(
                            it
                        )
                    }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


//            Spacer(modifier = Modifier.height(4.dp))
//            if (vaccineRequest.petGuardianName != null) {
//                Text(
//                    text = "Tutor: ${vaccineRequest.petGuardianName}",
//                    fontSize = 12.sp
//                )
//            }
            Spacer(modifier = Modifier.height(4.dp))

            if (vaccineRequest.animalName != null) {
                Text(
                    text = "Nome do Pet: ${vaccineRequest.animalName}",
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (showBottomSheet) {
                VaccineVeterinaryModalBottomSheet(
                    vaccineRequest = vaccineRequest,
                    onDismissRequest = { showBottomSheet = false },
                    goToUpdateVaccineRequestScreen = goToUpdateVaccineRequestScreen
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineVeterinaryModalBottomSheet(
    vaccineRequest: VaccineRequestResponse,
    onDismissRequest: () -> Unit,
    goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            if(vaccineRequest.status == "Expirado" || vaccineRequest.status == "Recusado") {
                Column {
                    vaccineRequest.status?.let { status ->
                        StatusIndicator(status = status.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 8.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Pet: ${vaccineRequest.animalName}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "A vacina está com o status ${vaccineRequest.status}. Caso queira realizar preencher uma nova solicitação de vacina para este pet, entre em contato com o tutor.")
                }

            } else if (vaccineRequest.status == "Aceito") {
                Column {
                    vaccineRequest.status?.let { status ->
                        StatusIndicator(status = status.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 8.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        buildAnnotatedString {
                            append("Você aceitou a solicitação de vacina do pet ")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(vaccineRequest.animalName)
                            }

                            append(". Preencha os dados específicos da aplicação da vacina para concluír o registro")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Button(onClick = { goToUpdateVaccineRequestScreen(vaccineRequest.id) }) {
                            Text(text = "Concluir registro de vacina")
                        }
                    }

                }
            } else if (vaccineRequest.status == "Aguardando_Assinatura") {
                Column {
                    vaccineRequest.status?.let { status ->
                        StatusIndicator(status = status.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 8.dp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        buildAnnotatedString {
                            append("Você preencheu os dados da solicitação de vacina do pet ")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(vaccineRequest.animalName)
                            }

                            append(". Você pode editar algum dado da solicitação de vacina ou realizar a assinatura digital da vacina!")
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { goToUpdateVaccineRequestScreen(vaccineRequest.id) }) {
                            Text(text = "Editar registro de vacina")
                        }
                        ButtonOpenLinkForDigitalSignatureOnBrowser(url = vaccineRequest.signUrl ?: "")
                    }
                }
            } else {
                VaccineInfoRow(vaccineRequest.vaccineApplication?.vaccine?.name, vaccineRequest.vaccineApplication?.applicationDate, true)
                Spacer(modifier = Modifier.height(8.dp))
                VaccineStatus(vaccineRequest.status, vaccineRequest.vaccineApplication?.applicationDate, true, true)
                Spacer(modifier = Modifier.height(8.dp))
                BatchInfoRow(vaccineRequest.vaccineApplication?.batchCode, vaccineRequest.vaccineApplication?.manufacturer, true)
                Spacer(modifier = Modifier.height(8.dp))
                PetInfoRow(vaccineRequest.animalName, "Nome do tutor", true)
                Spacer(modifier = Modifier.height(8.dp))
                NextApplicationDate(applicationDate = vaccineRequest.vaccineApplication?.nextDoseDate, true)
                Spacer(modifier = Modifier.height(8.dp))
                VaccineActions(status = vaccineRequest.status, pdfDocumentUrl = vaccineRequest.storagedDocumentSignedUrl, signatureUrl = vaccineRequest.signUrl, true, { goToUpdateVaccineRequestScreen(vaccineRequest.id) })
            }

        }
    }
}
//
//@Composable
//fun QRCodeScannerScreen() {
//    var scannedCode by remember { mutableStateOf<String?>(null) }
//    var isScanning by remember { mutableStateOf(false) }
//
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        if (isScanning) {
//            QRCodeScanner { code ->
//                scannedCode = code
//                isScanning = false
//            }
//        } else {
//            Button(onClick = { isScanning = true }) {
//                Text("Escanear QR Code")
//            }
//        }
//
//        scannedCode?.let {
//            Text("QR Code: $it", modifier = Modifier.padding(16.dp))
//        }
//    }
//}
//
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


//@Composable
//@Preview
//fun PreviewCardAssinado(){
//    var vaccine = VaccineRequestByVeterinary(
//        id = 1,
//        vaccine = Vaccine(
//            id = 2,
//            name = "Antirrábica"
//        ),
//        petGuardianName = "Gustavo Ferreira",
//        batchCode = "ABC22222",
//        animalName = "Calabreso",
//        status = "Assinado",
//        applicationDate = "20/03/2025",
//        manufacturer = "Biontech"
//    )
//    CarteiraPetTheme {
//        VaccineVeterinaryItem(vaccineRequest = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
//    }
//}
//
//@Composable
//@Preview
//fun PreviewCardPendente(){
//    var vaccine = VaccineRequestByVeterinary(
//        id = 1,
//        vaccine = Vaccine(
//            id = 2,
//            name = "Antirrábica"
//        ),
//        petGuardianName = "Gustavo Ferreira",
//        batchCode = "ABC22222",
//        animalName = "Calabreso",
//        status = "Pendente",
//        applicationDate = "20/03/2025",
//        manufacturer = "Biontech"
//    )
//    CarteiraPetTheme {
//        VaccineVeterinaryItem(vaccineRequest = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
//    }
//}
//
//@Composable
//@Preview
//fun PreviewCardRejeitado(){
//    var vaccine = VaccineRequestByVeterinary(
//        id = 1,
//        vaccine = null,
//        petGuardianName = "Gustavo Ferreira",
//        batchCode = null,
//        animalName = "Calabreso",
//        status = "Rejeitado",
//        applicationDate = null,
//        manufacturer = "Biontech"
//    )
//    CarteiraPetTheme {
//        VaccineVeterinaryItem(vaccineRequest = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
//    }
//}
//
//
//@Composable
//@Preview
//fun PreviewCardOutro(){
//    var vaccine = VaccineRequestByVeterinary(
//        id = 1,
//        vaccine = Vaccine(
//            id = 2,
//            name = "Antirrábica"
//        ),
//        petGuardianName = "Gustavo Ferreira",
//        batchCode = "ABC22222",
//        animalName = "Calabreso",
//        status = "Outro",
//        applicationDate = "20/03/2025",
//        manufacturer = "Biontech"
//    )
//    CarteiraPetTheme {
//        VaccineVeterinaryItem(vaccineRequest = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
//    }
//}
//
//@Composable
//@Preview
//fun PreviewOutro(){
//    var vaccine = VaccineRequestByVeterinary(
//        id = 1,
//        vaccine = null,
//        petGuardianName = "Gustavo Ferreira",
//        batchCode = null,
//        animalName = "Calabreso",
//        status = "Registro Incompleto",
//        applicationDate = null,
//        manufacturer = null
//    )
//    CarteiraPetTheme {
//        VaccineVeterinaryItem(vaccineRequest = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
//    }
//}