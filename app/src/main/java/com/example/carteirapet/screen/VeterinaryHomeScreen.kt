package com.example.carteirapet.screen

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.NextApplicationDate
import com.example.carteirapet.screen.components.PetInfoRow
import com.example.carteirapet.screen.components.VaccineActions
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.VaccineStatus
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
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.isLoading = true

        viewModel.loadVaccineRequestsFromVeterinary(onError = { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        })

        viewModel.isLoading = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
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
                            "Moo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Perfil do Usuário") },
                            onClick = {goToEditVeterinaryScreen()},
                            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Sair") },
                            onClick = {
                                viewModel.logout(
                                    goToLoginScreen,
                                    onError = { message ->
                                        Toast.makeText(
                                            context,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    })
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Logout,
                                    contentDescription = null
                                )
                            },
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(100.dp),
                onClick = {},
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Registrar Nova vacina") },
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
                if (viewModel.isLoading){
                    CircularProgressIndicator()
                } else {
                    VaccineRequests(viewModel.vaccines, goToUpdateVaccineRequestScreen)
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
fun VaccineVeterinaryItem(vaccine: VaccineRequestByVeterinary, goToUpdateVaccineRequestScreen: (vaccineRequestId: Int) -> Unit, modifier: Modifier) {
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

            VaccineInfoRow(vaccine.vaccine?.name, vaccine.applicationDate)
            VaccineStatus(vaccine.status, vaccine.applicationDate, false, true)
            Spacer(modifier = Modifier.height(4.dp))
            PetInfoRow(vaccine.animalName, vaccine.petGuardianName)
            Spacer(modifier = Modifier.height(4.dp))
            BatchInfoRow(vaccine.batchCode, vaccine.manufacturer)

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
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            VaccineInfoRow(vaccineRequest.vaccine?.name, vaccineRequest.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineStatus(vaccineRequest.status, vaccineRequest.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            BatchInfoRow(vaccineRequest.batchCode, vaccineRequest.manufacturer, true)
            Spacer(modifier = Modifier.height(8.dp))
            PetInfoRow(vaccineRequest.animalName, vaccineRequest.petGuardianName, true)
            Spacer(modifier = Modifier.height(8.dp))
            NextApplicationDate(applicationDate = vaccineRequest.applicationDate, true)
            Spacer(modifier = Modifier.height(8.dp))
            VaccineActions(status = vaccineRequest.status, pdfDocumentUrl = vaccineRequest.signedUrl, signatureUrl = vaccineRequest.storageUrl, true, { goToUpdateVaccineRequestScreen(vaccineRequest.id) })
        }
    }
}
