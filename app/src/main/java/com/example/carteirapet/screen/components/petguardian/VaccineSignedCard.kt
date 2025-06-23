package com.example.carteirapet.screen.components.petguardian

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.ButtonDownloadPdf
import com.example.carteirapet.screen.components.ButtonOpenPdfOnBrowser
import com.example.carteirapet.screen.components.StatusIndicator
import com.example.carteirapet.utils.DateUtils


@Composable
fun VaccineSignedCard(
    vaccineRequest: VaccineRequestResponse,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        VaccineSignedCardModalBottomSheet(
            vaccineRequest = vaccineRequest,
            onDismissRequest = { showBottomSheet = false }
        )
    }

    Card(
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Status no topo
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StatusIndicator(status = vaccineRequest.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nome da vacina em destaque
            vaccineRequest.vaccineApplication?.vaccine?.name?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Data de aplicação
            vaccineRequest.vaccineApplication?.applicationDate?.let {
                Text(
                    text = "Aplicação: ${DateUtils.formatDateStringToShow(it)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Nome do veterinário
            vaccineRequest.veterinaryDoctorName?.let {
                Text(
                    text = "Veterinário: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineSignedCardModalBottomSheet(
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StatusIndicator(status = vaccineRequest.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Esta solicitação de vacina foi preenchida assinada pelo seu médico veterinário com sucesso!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            vaccineRequest.vaccineApplication?.vaccine?.name?.let {
                LabelAndValue(label = "Vacina", value = it)
            }

            vaccineRequest.vaccineApplication?.manufacturer?.let {
                LabelAndValue(label = "Fabricante", value = it)
            }

            vaccineRequest.vaccineApplication?.batchCode?.let {
                LabelAndValue(label = "Lote", value = it)
            }

            vaccineRequest.vaccineApplication?.applicationDate?.let {
                LabelAndValue(
                    label = "Data da aplicação",
                    value = DateUtils.formatDateStringToShow(it)
                )
            }

            vaccineRequest.vaccineApplication?.nextDoseDate?.let {
                LabelAndValue(
                    label = "Próxima dose",
                    value = DateUtils.formatDateStringToShow(it)
                )
            }

            vaccineRequest.veterinaryDoctorName?.let {
                LabelAndValue(label = "Médico veterinário", value = it)
            }

            Spacer(modifier = Modifier.height(12.dp))

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
        }
    }
}
