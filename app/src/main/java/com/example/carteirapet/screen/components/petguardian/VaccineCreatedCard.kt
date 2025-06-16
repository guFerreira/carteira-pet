package com.example.carteirapet.screen.components.petguardian

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.StatusIndicator
import com.example.carteirapet.utils.DateUtils
import kotlinx.coroutines.delay

@Composable
fun VaccineCreatedCard(
    vaccineRequest: VaccineRequestResponse,
    goRegisterVaccineScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = goRegisterVaccineScreen,
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
                .padding(16.dp)
        ) {
            // Status no topo, centralizado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StatusIndicator(status = vaccineRequest.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Texto explicativo
            Text(
                text = "A vacina foi criada e está aguardando ser aceita pelo seu médico veterinário.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tempo restante até expiração
            vaccineRequest.expirationDate?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Ícone de tempo",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ExpirationCountdown(expirationDate = it)
                }
            }
        }
    }
}

@Composable
fun ExpirationCountdown(expirationDate: String) {
    var remainingText by remember { mutableStateOf(DateUtils.getTimeRemainingText(expirationDate)) }

    LaunchedEffect(expirationDate) {
        while (true) {
            remainingText = DateUtils.getTimeRemainingText(expirationDate)
            delay(1000L)
        }
    }

    Text(
        text = "Tempo restante até a expiração: $remainingText",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

