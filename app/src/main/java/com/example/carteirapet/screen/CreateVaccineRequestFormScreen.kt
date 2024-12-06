package com.example.carteirapet.screen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.carteirapet.R
import com.example.carteirapet.viewModels.CreateVaccineRequestFormViewModel
import com.example.carteirapet.viewModels.CreateVaccineRequestViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.format.TextStyle

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
    val vaccineId by viewModel::vaccineId
    val isLoading by viewModel::isLoading
    val showRedirectToZapSignUrl by viewModel::showRedirectToZapSignUrl
    val zapSignUrl by viewModel::zapSignUrl

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
                    OutlinedTextField(
                        value = applicationDate,
                        onValueChange = { viewModel.applicationDate = it },
                        label = { Text("Data de Aplicação *") },
                        modifier = Modifier.fillMaxWidth()
                    )
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
                    OutlinedTextField(
                        value = manufacturingDate,
                        onValueChange = { viewModel.manufacturingDate = it },
                        label = { Text("Data de Fabricação *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = expirationDate,
                        onValueChange = { viewModel.expirationDate = it },
                        label = { Text("Data de Validade *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nextDoseDate,
                        onValueChange = { viewModel.nextDoseDate = it },
                        label = { Text("Data da Próxima Dose *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = vaccineId.toString(),
                        onValueChange = { viewModel.vaccineId = it.toIntOrNull() ?: 0 },
                        label = { Text("ID da Vacina *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                        Text(text = "Atualizar")
                    }
                }
            }
            if (showRedirectToZapSignUrl && zapSignUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // Abrir o link no navegador
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(zapSignUrl))
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Text("Ir para o link de assinatura")
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
