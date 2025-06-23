package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
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
import com.example.carteirapet.screen.components.PetInformations
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.screen.components.SexDisplay
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
import com.example.carteirapet.screen.components.petguardian.VaccineSignedCard
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
                    Logo() // acho que faz sentido usar o logo aqui
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
        PullToRefreshBox(
            isRefreshing = viewModel.isLoadingVaccineRequests,
            onRefresh = {
                Toast.makeText(context, "Recarregando vacinas...", Toast.LENGTH_SHORT).show()

                if (petId != null) {
                    viewModel.loadVaccineRequests(petId, onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    })
                }
            }
        ) {
            LazyColumn(
                // Aplica o padding do Scaffold (para TopAppBar e FAB)
                contentPadding = innerPadding,
                // Espaçamento vertical entre cada item da lista
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (viewModel.isLoadingPetInformations) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    item {
                        PetInformations(viewModel.pet, modifier = Modifier.padding(horizontal = 16.dp). fillMaxWidth())
                    }

                    item {
                        // Adicione o padding horizontal ao cabeçalho da seção
                        Text(
                            text = "Vacinas",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp)
                        )
                    }

                    item {
                        // E também ao divisor
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }

                    if (viewModel.vaccineRequests.isEmpty() && !viewModel.isLoadingVaccineRequests) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Nenhuma vacina registrada \uD83D\uDC89",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(viewModel.vaccineRequests) { vaccineRequest ->
                            VaccinePetItem(
                                vaccineRequest = vaccineRequest,
                                goRegisterVaccineScreen = goRegisterVaccineScreen,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
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
    when (vaccineRequest.status.toUpperCase()) {
        "CRIADO" -> VaccineCreatedCard(vaccineRequest = vaccineRequest, goRegisterVaccineScreen)
        "RECUSADO" -> VaccineRejectedCard(vaccineRequest = vaccineRequest)
        "EXPIRADO" -> VaccineExpiredCard(vaccineRequest = vaccineRequest)
        "ACEITO" -> VaccineAcceptedCard(vaccineRequest = vaccineRequest)
        "AGUARDANDO_ASSINATURA" -> VaccineAwaitingSignatureCard(vaccineRequest = vaccineRequest)
        "ASSINADO" -> VaccineSignedCard(vaccineRequest = vaccineRequest)
        else -> {

        }
    }
    Spacer(modifier = Modifier.height(4.dp))
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
