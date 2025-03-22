package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.CardUser
import com.example.carteirapet.screen.components.Logo
import com.example.carteirapet.screen.components.NextApplicationDate
import com.example.carteirapet.screen.components.PetInfoRow
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.screen.components.VaccineActions
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.VaccineStatus
import com.example.carteirapet.ui.theme.CarteiraPetTheme
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
fun VaccineRequests(vaccines: List<VaccineRequestByVeterinary>, goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit) {
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
    vaccine: VaccineRequestByVeterinary,
    goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Card(
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                if (vaccine.vaccine?.name != null) {
                    Text(
                        text = vaccine.vaccine.name,
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


                vaccine.status?.let { status ->
                    StatusIndicator(status = status.replaceFirstChar { it.uppercase() }, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            vaccine.applicationDate?.let {
                Text(
                    text = "Data de aplicação: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            if (vaccine.petGuardianName != null) {
                Text(
                    text = "Tutor: ${vaccine.petGuardianName}",
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            if (vaccine.animalName != null) {
                Text(
                    text = "Nome do Pet: ${vaccine.animalName}",
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (showBottomSheet) {
                VaccineVeterinaryModalBottomSheet(
                    vaccineRequest = vaccine,
                    onDismissRequest = { showBottomSheet = false },
                    goToUpdateVaccineRequestScreen = goToUpdateVaccineRequestScreen
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: String, modifier: Modifier = Modifier) {
    val statusColor = when (status) {
        "Pendente" -> MaterialTheme.colorScheme.tertiaryContainer
        "Assinado" -> MaterialTheme.colorScheme.primaryContainer
        "Rejeitado" -> MaterialTheme.colorScheme.errorContainer
        "Registro Incompleto" -> MaterialTheme.colorScheme.secondaryContainer
        else -> MaterialTheme.colorScheme.inverseOnSurface
    }

    val icon = when (status) {
        "Pendente" -> Icons.Outlined.HourglassEmpty
        "Assinado" -> Icons.Outlined.CheckCircle
        "Rejeitado" -> Icons.Outlined.Cancel
        else -> Icons.Outlined.Info
    }

    val textColor = when (status) {
        "Pendente" -> MaterialTheme.colorScheme.onTertiaryContainer
        "Assinado" -> MaterialTheme.colorScheme.onPrimaryContainer
        "Rejeitado" -> MaterialTheme.colorScheme.onErrorContainer
        "Registro Incompleto" -> MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier
            .background(statusColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineVeterinaryModalBottomSheet(
    vaccineRequest: VaccineRequestByVeterinary,
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
            VaccineInfoRow(vaccineRequest.vaccine?.name, vaccineRequest.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineStatus(vaccineRequest.status, vaccineRequest.applicationDate, true, true)
            Spacer(modifier = Modifier.height(8.dp))
            BatchInfoRow(vaccineRequest.batchCode, vaccineRequest.manufacturer, true)
            Spacer(modifier = Modifier.height(8.dp))
            PetInfoRow(vaccineRequest.animalName, vaccineRequest.petGuardianName, true)
            Spacer(modifier = Modifier.height(8.dp))
            NextApplicationDate(applicationDate = vaccineRequest.nextDoseDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineActions(status = vaccineRequest.status, pdfDocumentUrl = vaccineRequest.storageUrl, signatureUrl = vaccineRequest.signedUrl, true, { goToUpdateVaccineRequestScreen(vaccineRequest.id) })
        }
    }
}

@Composable
@Preview
fun PreviewCardAssinado(){
    var vaccine = VaccineRequestByVeterinary(
        id = 1,
        vaccine = Vaccine(
            id = 2,
            name = "Antirrábica"
        ),
        petGuardianName = "Gustavo Ferreira",
        batchCode = "ABC22222",
        animalName = "Calabreso",
        status = "Assinado",
        applicationDate = "20/03/2025",
        manufacturer = "Biontech"
    )
    CarteiraPetTheme {
        VaccineVeterinaryItem(vaccine = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
fun PreviewCardPendente(){
    var vaccine = VaccineRequestByVeterinary(
        id = 1,
        vaccine = Vaccine(
            id = 2,
            name = "Antirrábica"
        ),
        petGuardianName = "Gustavo Ferreira",
        batchCode = "ABC22222",
        animalName = "Calabreso",
        status = "Pendente",
        applicationDate = "20/03/2025",
        manufacturer = "Biontech"
    )
    CarteiraPetTheme {
        VaccineVeterinaryItem(vaccine = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
fun PreviewCardRejeitado(){
    var vaccine = VaccineRequestByVeterinary(
        id = 1,
        vaccine = null,
        petGuardianName = "Gustavo Ferreira",
        batchCode = null,
        animalName = "Calabreso",
        status = "Rejeitado",
        applicationDate = null,
        manufacturer = "Biontech"
    )
    CarteiraPetTheme {
        VaccineVeterinaryItem(vaccine = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
    }
}


@Composable
@Preview
fun PreviewCardOutro(){
    var vaccine = VaccineRequestByVeterinary(
        id = 1,
        vaccine = Vaccine(
            id = 2,
            name = "Antirrábica"
        ),
        petGuardianName = "Gustavo Ferreira",
        batchCode = "ABC22222",
        animalName = "Calabreso",
        status = "Outro",
        applicationDate = "20/03/2025",
        manufacturer = "Biontech"
    )
    CarteiraPetTheme {
        VaccineVeterinaryItem(vaccine = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview
fun PreviewOutro(){
    var vaccine = VaccineRequestByVeterinary(
        id = 1,
        vaccine = null,
        petGuardianName = "Gustavo Ferreira",
        batchCode = null,
        animalName = "Calabreso",
        status = "Registro Incompleto",
        applicationDate = null,
        manufacturer = null
    )
    CarteiraPetTheme {
        VaccineVeterinaryItem(vaccine = vaccine, goToUpdateVaccineRequestScreen = {}, modifier = Modifier.fillMaxWidth())
    }
}