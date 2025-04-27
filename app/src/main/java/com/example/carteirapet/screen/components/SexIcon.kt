package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SexIcon(sex: String, modifier: Modifier = Modifier) {
    val (icon, color) = when (sex) {
        "MALE" -> Icons.Filled.Male to MaterialTheme.colorScheme.primary
        "FEMALE" -> Icons.Filled.Female to MaterialTheme.colorScheme.secondary
        else -> Icons.Filled.HelpOutline to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        imageVector = icon,
        contentDescription = sex,
        tint = color,
        modifier = modifier.size(20.dp)
    )
}