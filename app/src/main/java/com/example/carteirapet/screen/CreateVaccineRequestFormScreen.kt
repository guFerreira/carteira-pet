package com.example.carteirapet.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.carteirapet.viewModels.CreateVaccineRequestFormViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVaccineRequestFormScreen(
    vaccineRequestId: Int? = null,
    goToHomeScreen: () -> Unit,
    viewModel: CreateVaccineRequestFormViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val applicationDate by viewModel::applicationDate
    val applicationPlace by viewModel::applicationPlace
    val manufacturer by viewModel::manufacturer
    val batchCode by viewModel::batchCode
    val manufacturingDate by viewModel::manufacturingDate
    val expirationDate by viewModel::expirationDate
    val nextDoseDate by viewModel::nextDoseDate
    val selectedVaccine by viewModel::selectedVaccine
    val isLoading by viewModel::isLoading
    val showRedirectToZapSignUrl by viewModel::showRedirectToZapSignUrl
    val zapSignUrl by viewModel::zapSignUrl

    LaunchedEffect(Unit) {
        if (vaccineRequestId == null) {
            Toast.makeText(
                context,
                "Não foi possível carregar a solicitação de vacinação",
                Toast.LENGTH_SHORT
            ).show()
            return@LaunchedEffect
        }
        viewModel.loadData(vaccineRequestId, onError = { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    Scaffold(
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
                navigationIcon = {
                    IconButton(onClick = goToHomeScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column {
                    Text(text = if (viewModel.isEditing) "editando true" else "editando falso")
                    DropdownMenuField(
                        label = "Vacina",
                        options = viewModel.vaccineOptions,
                        selectedOption = viewModel.selectedVaccine,
                        onOptionSelected = { viewModel.selectedVaccine = it },
                        displayText = { it.name }
                    )

                    DateFieldInput(
                        "Data de Aplicação *",
                        applicationDate,
                        { viewModel.applicationDate = it })

                    OutlinedTextField(
                        value = applicationPlace,
                        onValueChange = { viewModel.applicationPlace = it },
                        label = { Text("Local de Aplicação *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = manufacturer,
                        onValueChange = { viewModel.manufacturer = it },
                        label = { Text("Fabricante *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = batchCode,
                        onValueChange = { viewModel.batchCode = it },
                        label = { Text("Código do Lote *") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DateFieldInput(
                        "Data de Fabricação *",
                        manufacturingDate,
                        { viewModel.manufacturingDate = it })

                    DateFieldInput(
                        "Data de Validade *",
                        expirationDate,
                        { viewModel.expirationDate = it })

                    DateFieldInput(
                        "Data da Próxima Dose *",
                        nextDoseDate,
                        { viewModel.nextDoseDate = it })

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (viewModel.zapSignUrl.isEmpty() && viewModel.isEditing) {
                            Button(
                                onClick = {
                                    vaccineRequestId?.let { id ->
                                        viewModel.updateVaccineRequest(id) { message ->
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                modifier = Modifier.width(200.dp)
                            ) {
                                Text(text = "Concluir solicitação de vacina")
                            }
                        }

                        if (viewModel.zapSignUrl.isNotEmpty()) {
                            if (viewModel.isEditing) {

                                FilledTonalButton(onClick = {
                                    viewModel.isEditing = false
                                }) {
                                    Text(text = "Cancelar")
                                }

                                Button(
                                    onClick = {
                                        vaccineRequestId?.let { id ->
                                            viewModel.updateVaccineRequest(id) { message ->
                                                Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    },
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    Text(text = "Concluir solicitação de vacina")
                                }
                            }

                            if (!viewModel.isEditing) {
                                Button(
                                    onClick = {
                                        viewModel.isEditing = true
                                    },
                                    modifier = Modifier.width(200.dp)
                                ) {
                                    Text(text = "Editar solicitação de vacina")
                                }

                                Button(onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zapSignUrl))
                                    ContextCompat.startActivity(context, intent, null)
                                }) {
                                    Text("Ir para o link de assinatura")
                                }
                            }
                        }
                    }

                }
            }

        }
    }
}


@Preview
@Composable
fun CreateVaccineRequestFormScreenPreview() {
    CreateVaccineRequestFormScreen(1, {})
}
