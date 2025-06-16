package com.example.carteirapet.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.TimerOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicator(status: String, modifier: Modifier = Modifier) {
    var statusName = status.toUpperCase()

    val statusColor = when (statusName) {
        "CRIADO" -> Color(0xFFD0E8FF) // Azul claro
        "AGUARDANDO_ASSINATURA" -> Color(0xFFFFF3CD) // Amarelo claro
        "ACEITO" -> Color(0xFFD1E7DD) // Verde claro
        "ASSINADO" -> Color(0xFF2E8B57) // Verde escuro
        "RECUSADO" -> Color(0xFFF8D7DA) // Vermelho claro
        "EXPIRADO" -> Color(0xFFE0E0E0) // Cinza claro
        else -> Color(0xFFE5E5E5) // Cinza neutro
    }

    val textColor = when (statusName) {
        "CRIADO" -> Color(0xFF084298)
        "AGUARDANDO_ASSINATURA" -> Color(0xFF856404)
        "ACEITO" -> Color(0xFF0F5132)
        "ASSINADO" -> Color.White
        "RECUSADO" -> Color(0xFF842029)
        "EXPIRADO" -> Color(0xFF6C757D)
        else -> Color(0xFF6C757D)
    }

//
//    val statusColor = when (statusName) {
//        "CRIADO" -> MaterialTheme.colorScheme.tertiaryContainer
//        "RECUSADO" -> MaterialTheme.colorScheme.errorContainer
//        "EXPIRADO" -> MaterialTheme.colorScheme.errorContainer
//        "ACEITO" -> MaterialTheme.colorScheme.primaryContainer
//        "AGUARDANDO_ASSINATURA" -> MaterialTheme.colorScheme.tertiaryContainer
//        "ASSINADO" -> Color(0xFF2E8B57)
//        else -> MaterialTheme.colorScheme.inverseOnSurface
//    }

//    val textColor = when (statusName) {
//        "CRIADO" -> MaterialTheme.colorScheme.onTertiaryContainer
//        "RECUSADO" -> MaterialTheme.colorScheme.onErrorContainer
//        "EXPIRADO" -> MaterialTheme.colorScheme.onErrorContainer
//        "ACEITO" -> MaterialTheme.colorScheme.onPrimaryContainer
//        "AGUARDANDO_ASSINATURA" -> MaterialTheme.colorScheme.onTertiaryContainer
//        "ASSINADO" -> Color.White
//        else -> MaterialTheme.colorScheme.onSurfaceVariant
//    }

    val icon = when (statusName) {
        "CRIADO" -> Icons.Outlined.Add
        "RECUSADO" -> Icons.Outlined.Cancel
        "EXPIRADO" -> Icons.Outlined.TimerOff
        "ACEITO" -> Icons.Outlined.CheckCircle
        "AGUARDANDO_ASSINATURA" -> Icons.Outlined.HourglassEmpty
        "ASSINADO" -> Icons.Outlined.CheckCircle
        else -> Icons.Outlined.Info
    }

    val text = when (statusName) {
        "CRIADO" -> "Criado"
        "RECUSADO" -> "Recusado"
        "EXPIRADO" -> "Expirado"
        "ACEITO" -> "Aceito"
        "AGUARDANDO_ASSINATURA" -> "Aguardando Assinatura"
        "ASSINADO" -> "Assinado"
        else -> "Indefinido"
    }

    Row(
        modifier = modifier
            .background(statusColor, shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = textColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun StatusIndicatorPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusIndicator(status = "CRIADO")
            StatusIndicator(status = "RECUSADO")
            StatusIndicator(status = "EXPIRADO")
            StatusIndicator(status = "ACEITO")
            StatusIndicator(status = "AGUARDANDO_ASSINATURA")
            StatusIndicator(status = "ASSINADO")
            StatusIndicator(status = "INDEFINIDO")
        }
    }
}