package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineApplication
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.ButtonDownloadPdf
import com.example.carteirapet.screen.components.ButtonOpenPdfOnBrowser
import com.example.carteirapet.screen.components.Logo
import com.example.carteirapet.screen.components.NextApplicationDate
import com.example.carteirapet.screen.components.PetImage
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.screen.components.SexIcon
import com.example.carteirapet.screen.components.StatusIndicator
import com.example.carteirapet.screen.components.petguardian.VaccineAcceptedCard
import com.example.carteirapet.screen.components.VaccineActions
import com.example.carteirapet.screen.components.petguardian.VaccineCreatedCard
import com.example.carteirapet.screen.components.petguardian.VaccineExpiredCard
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.petguardian.VaccineRejectedCard
import com.example.carteirapet.screen.components.VaccineStatus
import com.example.carteirapet.screen.components.VeterinaryInfoRow
import com.example.carteirapet.screen.components.petguardian.VaccineAwaitingSignatureCard
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.utils.DateUtils
import com.example.carteirapet.viewModels.PetInformationViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInformation(
    petId: Int? = null,
    goToHomeScreen: () -> Unit,
    goRegisterVaccineScreen: () -> Unit,
    viewModel: PetInformationViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    LaunchedEffect(petId) {

        if (petId != null) {
            viewModel.loadPetInformation(petId, onError = { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            })
        }
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
                navigationIcon = {
                    IconButton(onClick = goToHomeScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onSurface
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
                onClick = goRegisterVaccineScreen,
                icon = { Icon(Icons.Filled.Add, "Registrar vacina") },
                text = { Text(text = "Registrar vacina") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .safeContentPadding()
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            if (viewModel.isLoadingPetInformations) {
                CircularProgressIndicator()
            } else {
                PetInformations(viewModel.pet)

                Spacer(modifier = Modifier.height(12.dp))

                PullToRefreshBox(
                    isRefreshing = viewModel.isLoadingVaccineRequests,
                    onRefresh = {
                        if (petId != null) {
                            viewModel.loadVaccineRequests(petId, onError = { message ->
                                Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }
                    }) {

                    Column {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Vacinas",
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }
                        Vaccines(viewModel.vaccineRequests, viewModel.isLoadingVaccineRequests, goRegisterVaccineScreen)
                    }
                }
            }
        }
    }
}

@Composable
fun PetInformations(pet: Animal?) {
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (pet == null) {
            Text(text = "Carregando informações do pet...")
        } else {
            PetImage(pet = pet, true)

            Column {
                Text(
                    text = pet.name, style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                    )
                )
                Text(
                    text = "Raça: ${pet.breeds.first().name}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )
                Text(
                    text = "Data de Nascimento: ${pet.birthDate}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )

                Row (verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Sexo:", style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 14.sp,
                        )
                    )
                    SexIcon(pet.sex)
                }
                Text(
                    text = "Castrado: ${if (pet.neutered == true) "Sim" else "Não"}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 14.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun Vaccines(vaccineRequests: List<VaccineRequestResponse>, isLoading: Boolean = false, goRegisterVaccineScreen: () -> Unit) {
    LazyColumn() {
        if (vaccineRequests.isEmpty() && !isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nenhuma vacina registrada \uD83D\uDC89")
                }
            }
        } else {
            items(vaccineRequests.size) { item ->
                VaccinePetItem(vaccineRequests[item], goRegisterVaccineScreen,  modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun VaccinePetItem(
    vaccineRequest: VaccineRequestResponse,
    goRegisterVaccineScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    when (vaccineRequest.status.toUpperCase()) {
        "CRIADO" -> VaccineCreatedCard(vaccineRequest = vaccineRequest, goRegisterVaccineScreen)
        "RECUSADO" -> VaccineRejectedCard(vaccineRequest = vaccineRequest)
        "EXPIRADO" -> VaccineExpiredCard(vaccineRequest = vaccineRequest)
        "ACEITO" -> VaccineAcceptedCard(vaccineRequest = vaccineRequest)
        "AGUARDANDO_ASSINATURA" -> VaccineAwaitingSignatureCard(vaccineRequest = vaccineRequest)
        "ASSINADO" -> VaccineAcceptedCard(vaccineRequest = vaccineRequest)
        else -> {
            Card(
                onClick = { showBottomSheet = true },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        StatusIndicator(status = vaccineRequest.status)

                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    var requestStatus = vaccineRequest.status.toUpperCase()
                    when (requestStatus) {
                        "ASSINADO" -> {
                            VaccineInfoRow(
                                vaccineName = vaccineRequest.vaccineApplication?.vaccine?.name,
                                applicationDate = vaccineRequest.vaccineApplication?.applicationDate?.let {
                                    DateUtils.formatDateStringToShow(
                                        it
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            VeterinaryInfoRow(
                                vaccineRequest.veterinaryDoctorName,
                                crmv = ""
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            BatchInfoRow(
                                batchCode = vaccineRequest.vaccineApplication?.batchCode,
                                manufacturer = vaccineRequest.vaccineApplication?.manufacturer
                            )
                        }
                        else -> {
                            Text(
                                text = "O status da solicitação de vacina é indefinido",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            if (showBottomSheet) {
                VaccinePetModalBottomSheet(
                    vaccineRequest = vaccineRequest,
                    onDismissRequest = { showBottomSheet = false }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinePetModalBottomSheet(
    vaccineRequest: VaccineRequestResponse,
    onDismissRequest: () -> Unit,
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

            if(vaccineRequest.status == "Aguardando_Assinatura"){
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    StatusIndicator(status = vaccineRequest.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Vacina: ${vaccineRequest.vaccineApplication?.vaccine?.name}")
                Text(text = "Aplicada em: ${vaccineRequest.vaccineApplication?.applicationDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")
                Text(text = "Local de Aplicação: ${vaccineRequest.vaccineApplication?.applicationPlace}")
                vaccineRequest.vaccineApplication?.nextDoseDate.let {
                    Text(text = "Próxima dose: ${it?.let { it1 ->
                        DateUtils.formatDateStringToShow(
                            it1
                        )
                    }}")
                }
                Text(text = "Lote: ${vaccineRequest.vaccineApplication?.batchCode}")
                Text(text = "Fabricante: ${vaccineRequest.vaccineApplication?.manufacturer}")
                Text(text = "Data de Fabricação:${vaccineRequest.vaccineApplication?.manufacturingDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")
                Text(text = "Data de Expiração: ${vaccineRequest.vaccineApplication?.expirationDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")
            } else if(vaccineRequest.status == "Assinado"){
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    StatusIndicator(status = vaccineRequest.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Vacina: ${vaccineRequest.vaccineApplication?.vaccine?.name}")
                Text(text = "Aplicada em: ${vaccineRequest.vaccineApplication?.applicationDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")
                Text(text = "Local de Aplicação: ${vaccineRequest.vaccineApplication?.applicationPlace}")
                vaccineRequest.vaccineApplication?.nextDoseDate.let {
                    Text(text = "Próxima dose: ${it?.let { it1 ->
                        DateUtils.formatDateStringToShow(
                            it1
                        )
                    }}")
                }
                Text(text = "Lote: ${vaccineRequest.vaccineApplication?.batchCode}")
                Text(text = "Fabricante: ${vaccineRequest.vaccineApplication?.manufacturer}")
                Text(text = "Data de Fabricação:${vaccineRequest.vaccineApplication?.manufacturingDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")
                Text(text = "Data de Expiração: ${vaccineRequest.vaccineApplication?.expirationDate?.let {
                    DateUtils.formatDateStringToShow(
                        it
                    )
                }}")


                Divider(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    vaccineRequest.storagedDocumentSignedUrl?.let { ButtonDownloadPdf(pdfUrl = it) }
                    vaccineRequest.storagedDocumentSignedUrl?.let { ButtonOpenPdfOnBrowser(pdfUrl = it) }
                }
            }else{
                VaccineInfoRow(vaccineRequest.vaccineApplication?.vaccine?.name,
                    vaccineRequest.vaccineApplication?.applicationDate, true)
                Spacer(modifier = Modifier.height(8.dp))
                VaccineStatus(vaccineRequest.status, vaccineRequest.vaccineApplication?.applicationDate, true)
                Spacer(modifier = Modifier.height(8.dp))
                BatchInfoRow(vaccineRequest.vaccineApplication?.batchCode, vaccineRequest.vaccineApplication?.manufacturer, true)
                Spacer(modifier = Modifier.height(8.dp))
                VeterinaryInfoRow(vaccineRequest.veterinaryDoctorName, "vaccineRequest.crmv", true)
                Spacer(modifier = Modifier.height(8.dp))
                NextApplicationDate(applicationDate = vaccineRequest.vaccineApplication?.nextDoseDate, true)
                Spacer(modifier = Modifier.height(8.dp))
                VaccineActions(
                    status = vaccineRequest.status,
                    pdfDocumentUrl = vaccineRequest.storagedDocumentSignedUrl,
                    signatureUrl = null,
                    isVeterinary = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaccinePetItemPreview() {
    CarteiraPetTheme {
        val mockVaccineRequest = { status: String ->
            VaccineRequestResponse(
                id = 1,
                status = status,
                vaccineApplication = when (status) {
                    "AGUARDANDO_ASSINATURA", "ASSINADO", "ACEITO" -> VaccineApplication(
                        id = 101,
                        vaccine = Vaccine(name = "Raiva", id = 1),
                        applicationDate = "2025-05-01",
                        applicationPlace = "Clínica PetVida",
                        batchCode = "ABC123",
                        manufacturer = "PetVax",
                        manufacturingDate = "2024-12-01",
                        expirationDate = "2026-12-01",
                        nextDoseDate = "2026-05-01"
                    )
                    else -> null
                },
                animalName = "Rex",
                animalSpecies = "Cão",
                veterinaryDoctorName = if (status == "AGUARDANDO_ASSINATURA" || status == "ASSINADO" || status == "ACEITO" || status == "RECUSADO") "Dr. João Silva" else null,
                requestDate = "2025-04-20",
                expirationDate = "2025-06-01",
                acceptanceDate = if (status == "ACEITO" || status == "ASSINADO" || status == "AGUARDANDO_ASSINATURA") "2025-04-25" else null,
                storagedDocumentSignedUrl = if (status == "ASSINADO") "https://storage.example.com/vaccine/1/signed.pdf" else null,
                signUrl = if (status == "AGUARDANDO_ASSINATURA") "https://sign.example.com/vaccine/1" else null
            )
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = listOf(
                "CRIADO",
                "RECUSADO",
                "EXPIRADO",
                "ACEITO",
                "AGUARDANDO_ASSINATURA",
                "ASSINADO",
                "INDEFINIDO"
            )) { status ->
                VaccinePetItem(
                    vaccineRequest = mockVaccineRequest(status),
                    {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
