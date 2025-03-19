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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.VaccineRequestByAnimal
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.NextApplicationDate
import com.example.carteirapet.screen.components.PetImage
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.screen.components.VaccineActions
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.VaccineStatus
import com.example.carteirapet.screen.components.VeterinaryInfoRow
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Pets,
                            contentDescription = "Pets Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Carteirinha",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

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
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (viewModel.isLoadingPetInformations) {
                    CircularProgressIndicator()
                } else {
                    PetInformations(viewModel.pet)

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
                        Vaccines(viewModel.vaccineRequests, viewModel.isLoadingVaccineRequests)
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
                Text(
                    text = "Sexo: ${pet.sex}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )
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
fun Vaccines(vaccines: List<VaccineRequestByAnimal>, isLoading: Boolean = false) {
    LazyColumn() {
        if (vaccines.isEmpty() && !isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nenhuma vacina registrada \uD83D\uDC89")
                }
            }
        } else {
            items(vaccines.size) { item ->
                VaccinePetItem(vaccines[item], modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun VaccinePetItem(vaccine: VaccineRequestByAnimal, modifier: Modifier) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,// Defina a cor desejada aqui
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer // Defina a cor do texto desejada aqui
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            VaccineInfoRow(vaccine.vaccineName, vaccine.applicationDate)
            VaccineStatus(vaccine.status, vaccine.applicationDate)
            Spacer(modifier = Modifier.height(4.dp))
            VeterinaryInfoRow(vaccine.veterinaryDoctorName, vaccine.crmv)
            Spacer(modifier = Modifier.height(4.dp))
            BatchInfoRow(vaccine.batchCode, vaccine.manufacturer)

            if (showBottomSheet) {
                VaccinePetModalBottomSheet(
                    vaccine = vaccine,
                    onDismissRequest = { showBottomSheet = false }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinePetModalBottomSheet(
    vaccine: VaccineRequestByAnimal,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            VaccineInfoRow(vaccine.vaccineName, vaccine.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineStatus(vaccine.status, vaccine.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            BatchInfoRow(vaccine.batchCode, vaccine.manufacturer, true)
            Spacer(modifier = Modifier.height(8.dp))
            VeterinaryInfoRow(vaccine.veterinaryDoctorName, vaccine.crmv, true)
            Spacer(modifier = Modifier.height(8.dp))
            NextApplicationDate(applicationDate = vaccine.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineActions(
                status = vaccine.status,
                pdfDocumentUrl = vaccine.storage,
                signatureUrl = null,
                isVeterinary = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VaccineModalBottomSheetPreview() {
    var showBottomSheet by remember { mutableStateOf(true) }
    if (showBottomSheet) {
        VaccinePetModalBottomSheet(
            VaccineRequestByAnimal(
                id = 1,
                status = "assinado",
                vaccineName = "Antirabica",
                applicationDate = "20/10/2024",
                batchCode = "1234",
                manufacturer = "Astrazenica",
                veterinaryDoctorName = "Cristiano Ronaldo Siiiiiuuu",
                crmv = "CRMV-RS 12345",
                nextDoseDate = "20/11/2024",
                storage = "",
                signedUrl = ""
            ),
            onDismissRequest = { showBottomSheet = false }
        )
    }
}


@Composable
@Preview
fun PetInformationPreview1() {
    Column {
        VaccinePetItem(
            vaccine = VaccineRequestByAnimal(
                id = 1,
                status = "pendente",
                vaccineName = null,
                applicationDate = null,
                batchCode = null,
                manufacturer = null,
                veterinaryDoctorName = null,
                crmv = null,
                nextDoseDate = null,
                storage = null,
                signedUrl = null
            ), modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        VaccinePetItem(
            vaccine = VaccineRequestByAnimal(
                id = 1,
                status = "pendente",
                vaccineName = "Antirabica",
                applicationDate = "20/10/2024",
                batchCode = "1234",
                manufacturer = "Astrazenica",
                veterinaryDoctorName = "Cristiano Ronaldo Siiiiiuuu",
                crmv = "CRMV-RS 12345",
                nextDoseDate = null,
                storage = null,
                signedUrl = null
            ), modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        VaccinePetItem(
            vaccine = VaccineRequestByAnimal(
                id = 1,
                status = "pendente",
                vaccineName = "Antirabica",
                applicationDate = "20/10/2024",
                batchCode = "1234",
                manufacturer = "Astrazenica",
                veterinaryDoctorName = "Cristiano Ronaldo Siiiiiuuu",
                crmv = "CRMV-RS 12345",
                nextDoseDate = "20/11/2024",
                storage = null,
                signedUrl = null
            ), modifier = Modifier.padding(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        VaccinePetItem(
            vaccine = VaccineRequestByAnimal(
                id = 1,
                status = "assinado",
                vaccineName = "Antirabica",
                applicationDate = "20/10/2024",
                batchCode = "1234",
                manufacturer = "Astrazenica",
                veterinaryDoctorName = "Cristiano Ronaldo Siiiiiuuu",
                crmv = "CRMV-RS 12345",
                nextDoseDate = "20/11/2024",
                storage = null,
                signedUrl = null
            ), modifier = Modifier.padding(10.dp)
        )

    }
}