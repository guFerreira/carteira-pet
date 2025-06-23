package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CardUser(userName: String, isVeterinary: Boolean? = false) {
    // Usamos um Card do Material 3 para dar a ele a aparência de um cartão
    Card(
        colors = CardDefaults.cardColors(
            // surfaceContainerHigh é uma boa escolha para um card de destaque
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Adiciona uma elevação sutil
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp) // Espaçamento vertical para o card, separando-o do topo e da lista
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Padding interno para o conteúdo do card
                .fillMaxWidth()
        ) {
            Text(
                text = "Olá, ${userName}!",
                // Usando headlineSmall para um título de destaque, mais impactante
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface // Cor principal para o texto
            )

            Spacer(modifier = Modifier.height(8.dp)) // Espaço entre título e subtítulo (8dp é um bom padrão)

            Text(
                text = if (isVeterinary == true) {
                    "Você pode visualizar as solicitações de vacinas relacionadas a você!"
                } else {
                    "Selecione um de seus pets para visualizar as suas vacinas"
                },
                // Usando bodyLarge para o texto de corpo, mais legível
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Cor secundária para o subtítulo
            )
        }
    }
}