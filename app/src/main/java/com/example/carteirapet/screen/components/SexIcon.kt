package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SexIcon(
    sex: String,
    modifier: Modifier = Modifier,
    tintColor: Color? = null
) {
    val (icon, defaultColor) = when (sex) {
        "MALE" -> Icons.Filled.Male to MaterialTheme.colorScheme.primary
        "FEMALE" -> Icons.Filled.Female to MaterialTheme.colorScheme.secondary
        else -> Icons.Filled.HelpOutline to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        imageVector = icon,
        contentDescription = sex,
        tint = tintColor ?: defaultColor,
        modifier = modifier.size(20.dp)
    )
}

@Composable
fun SexDisplay(sex: String, color: Color = MaterialTheme.colorScheme.onPrimaryContainer) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SexIcon(sex = sex, tintColor = color)
        Text(
            text = when (sex) {
                "MALE" -> "Macho"
                "FEMALE" -> "Fêmea"
                else -> "Desconhecido"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}
