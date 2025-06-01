package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carteirapet.repositories.VaccineRequestResponse

@Composable
fun VaccineCreatedCard(
    vaccineRequest: VaccineRequestResponse,
    goRegisterVaccineScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = goRegisterVaccineScreen,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            StatusIndicator(status = vaccineRequest.status)

            Text(
                text = "A vacina foi criada e está aguardando ser aceita pelo seu médico veterinário",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "Tempo de expiração: ${vaccineRequest.expirationDate}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
