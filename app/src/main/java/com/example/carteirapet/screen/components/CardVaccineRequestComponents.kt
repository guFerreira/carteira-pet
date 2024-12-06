package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun VaccineInfoRow(vaccineName: String?, applicationDate: String?, isShowOnModal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (vaccineName != null) {
            Text(
                text = vaccineName,
                fontSize = if (isShowOnModal) 24.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        if (applicationDate != null) {
            Text(
                text = applicationDate,
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun VaccineStatus(status: String?, applicationDate: String?, isShowOnModal: Boolean = false) {
    if (status != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            if (status == "pendente" && applicationDate == null) {
                Text(
                    text = "Status: ${"Aceito".toUpperCase()}",
                    fontSize = if (isShowOnModal) 18.sp else 12.sp,
                )
            } else {
                Text(
                    text = "Status: ${status.toUpperCase()}",
                    fontSize = if (isShowOnModal) 18.sp else 12.sp,
                )
            }

            if (status == "assinado") {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Assinado",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
        if (status == "pendente" && applicationDate == null) {
            Text(
                text = "Esta é uma solicitação aceita pelo seu médico veterinário que ainda não foi concluída. Peça para o veterinário concluir o registro da vacina!",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun PetInfoRow(animalName: String?, petGuardianName: String?, isShowOnModal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (animalName != null) {
            Text(
                text = "Médico Veterinário: $animalName",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        if (petGuardianName != null) {
            Text(
                text = petGuardianName,
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun VeterinaryInfoRow(veterinaryDoctorName: String?, crmv: String?, isShowOnModal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (veterinaryDoctorName != null) {
            Text(
                text = "Médico Veterinário: $veterinaryDoctorName",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        if (crmv != null) {
            Text(
                text = crmv,
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun NextApplicationDate(applicationDate: String?, isShowOnModal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (applicationDate != null) {
            Text(
                text = if (applicationDate.isEmpty()) "Não há próxima dose" else "Data da próxima dose: $applicationDate",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
fun BatchInfoRow(batchCode: String?, manufacturer: String?, isShowOnModal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (batchCode != null) {
            Text(
                text = "Lote: $batchCode",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        if (manufacturer != null) {
            Text(
                text = "Fabricante: $manufacturer",
                fontSize = if (isShowOnModal) 18.sp else 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}