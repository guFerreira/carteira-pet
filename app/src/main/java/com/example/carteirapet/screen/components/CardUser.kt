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
fun CardUser(name: String, isVeterinary: Boolean? = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 24.dp) // Padding externo para alinhamento correto
    ) {
        Text(
            text = "Olá, ${name}!",
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espaço entre título e subtítulo

        Text(
            text = if (isVeterinary == true) {
                "Você pode visualizar as solicitações de vacinas relacionadas a você!"
            } else {
                "Selecione um de seus pets para visualizar as suas vacinas"
            },
            fontWeight = FontWeight.Light,
            fontSize = 16.sp, // Tamanho adequado para texto secundário
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}