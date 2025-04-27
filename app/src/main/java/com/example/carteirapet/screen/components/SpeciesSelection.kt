package com.example.carteirapet.screen.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.ui.theme.CarteiraPetTheme

// Lista de espécies com ícones e cores (pode ser movida para o ViewModel)
data class SpeciesOption(
    val name: String,
    val label: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val fontColor: Color,
    val borderColor: Color
)

@Composable
fun SpeciesSelection(
    selectedSpecies: String,
    onSpeciesSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

//    MaterialTheme.colorScheme.primaryContainer,
//    MaterialTheme.colorScheme.onPrimaryContainer,
//    MaterialTheme.colorScheme.onPrimaryContainer
//
//    MaterialTheme.colorScheme.secondaryContainer,
//    MaterialTheme.colorScheme.onSecondaryContainer,
//    MaterialTheme.colorScheme.onSecondaryContainer
//
//    MaterialTheme.colorScheme.tertiaryContainer,
//    MaterialTheme.colorScheme.onTertiaryContainer,
//    MaterialTheme.colorScheme.onTertiaryContainer

    val speciesOptions = listOf(
        SpeciesOption(
            "dog",
            "Cachorro",
            Icons.Filled.Pets,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        ), // Verde para cachorro
        SpeciesOption(
            "cat",
            "Gato",
            Icons.Filled.Pets,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer
        )   // Laranja para gato
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título
            Text(
                text = "O seu amigo peludo é um? 🐾",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,

                )
            Spacer(modifier = Modifier.height(12.dp))

            // Opções de espécie
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                speciesOptions.forEach { option ->
                    SpeciesOptionCard(
                        option = option,
                        isSelected = selectedSpecies == option.name,
                        onClick = { onSpeciesSelected(option.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeciesOptionCard(
    option: SpeciesOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animação de cor para fundo e borda
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) option.backgroundColor.copy(alpha = 1f) else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = tween(300)
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) option.borderColor.copy(alpha = 1f) else MaterialTheme.colorScheme.outline,
        animationSpec = tween(300)
    )

    // Interação para ripple effect
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .border( if (isSelected) 3.dp else 2.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // Define o containerColor como backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp) // Adiciona padding interno
                .semantics { contentDescription = "Selecionar ${option.label}" }
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (option.name) {
                    "dog" -> "🐶"
                    "cat" -> "🐱"
                    else -> option.label
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 24.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = when (option.name) {
                    "dog" -> "Cachorro"
                    "cat" -> "Gato"
                    else -> option.label
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) option.fontColor else MaterialTheme.colorScheme.onSurface,
                fontSize = if (isSelected) 18.sp else 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "SpeciesSelection - Dog Selected",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SpeciesSelectionPreviewDog() {
    CarteiraPetTheme {
        SpeciesSelection(
            selectedSpecies = "dog",
            onSpeciesSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    name = "SpeciesSelection - Cat Selected",
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SpeciesSelectionPreviewCat() {
    CarteiraPetTheme {
        SpeciesSelection(
            selectedSpecies = "cat",
            onSpeciesSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    name = "SpeciesSelection - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SpeciesSelectionPreviewDark() {
    CarteiraPetTheme {
        SpeciesSelection(
            selectedSpecies = "dog",
            onSpeciesSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    name = "SpeciesSelection - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SpeciesSelectionPreviewDarkCat() {
    CarteiraPetTheme {
        SpeciesSelection(
            selectedSpecies = "cat",
            onSpeciesSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}