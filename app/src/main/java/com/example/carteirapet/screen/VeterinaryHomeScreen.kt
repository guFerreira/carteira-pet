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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carteirapet.repositories.VaccineRequestByVeterinary
import com.example.carteirapet.screen.components.BatchInfoRow
import com.example.carteirapet.screen.components.VaccineInfoRow
import com.example.carteirapet.screen.components.VaccineStatus
import com.example.carteirapet.viewModels.VeterinaryHomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinaryHomeScreen(
    goToLoginScreen: () -> Unit,
    goToEditVeterinaryScreen: () -> Unit,
    viewModel: VeterinaryHomeViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
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
                            onClick = {},
                            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Sair") },
                            onClick = {

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
                    VaccineRequests(vaccineRequests)
               }
            }
        }
    }
}




@Composable
fun VaccineRequests(vaccines: List<VaccineRequestByVeterinary>) {
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
                VaccineVeterinaryItem(vaccines[item], modifier = Modifier.padding(4.dp))
            }
        }
    }

}

//data class VaccineRequestByVeterinary(
//    val id: Int,
//    val status: String,
//    val vaccineName: String?,
//    val applicationDate: String?,
//    val batchCode: String?,
//    val manufacturer: String?,
//    val nextDoseDate: String?,
//    val animalName: String?,
//    val petGuardianName: String?,
//    val storageUrl: String?,
//    val signedUrl: String?
//)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineVeterinaryItem(vaccine: VaccineRequestByVeterinary, modifier: Modifier) {
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
            PetInfoRow(vaccine.animalName, vaccine.petGuardianName)
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

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VaccineRequestItem(vaccine: Vaccine, modifier: Modifier) {
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
//    var showBottomSheet by remember { mutableStateOf(false) }
//    Card(
//        onClick = { showBottomSheet = true },
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//        modifier = modifier,
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
//        )
//    ) {
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            Text(
//                text = vaccine.applicationDate,
//                fontSize = 12.sp,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = vaccine.name,
//                fontSize = 16.sp,
//                lineHeight = 24.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 4.dp)
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Pet: ${"Mia"}",
//                fontSize = 14.sp,
//                modifier = Modifier.padding(bottom = 0.dp)
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Tutor: ${"João Teste"}",
//                fontSize = 14.sp,
//                modifier = Modifier.padding(bottom = 0.dp)
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Status da assinatura: ${vaccine.status}",
//                fontSize = 14.sp,
//                modifier = Modifier.padding(bottom = 0.dp)
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            HorizontalDivider(modifier = Modifier.height(1.dp))
//
//            if (showBottomSheet) {
//                ModalBottomSheet(
//                    onDismissRequest = {
//                        showBottomSheet = false
//                    },
//                    sheetState = sheetState,
//                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = vaccine.name,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 8.dp)
//                        )
//                        Text(
//                            text = "Data da aplicação: ${vaccine.applicationDate}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        Text(
//                            text = "Lote: ${vaccine.batchCode}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        Text(
//                            text = "Fabricante: ${vaccine.manufacturer}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        Text(
//                            text = "Médico veterinário: ${vaccine.veterinaryDoctorName}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 4.dp)
//                        )
//                        Text(
//                            text = "CRMV: ${vaccine.crmv}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//                        Text(
//                            text = "Status do documento: ${vaccine.status}",
//                            fontSize = 14.sp,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//                        Divider(
//                            color = MaterialTheme.colorScheme.onTertiaryContainer,
//                            modifier = Modifier.padding(vertical = 8.dp)
//                        )
//                        Text(
//                            text = "Documento PDF",
//                            fontSize = 14.sp,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 4.dp)
//                        )
//                        Row {
//                            AbrirPdfNoNavegador(pdfUrl = vaccine.signedPdf)
//                            FazerDownloadPdf(pdfUrl = vaccine.signedPdf)
//                        }
//                    }
//
//                }
//            }
//        }
//    }
//}




@Preview
@Composable
fun VaccineRequestItemPreview() {
    VeterinaryHomeScreen(1)
}
