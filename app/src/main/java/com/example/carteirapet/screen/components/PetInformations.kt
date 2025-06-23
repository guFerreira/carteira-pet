package com.example.carteirapet.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.carteirapet.repositories.Animal

@OptIn(ExperimentalLayoutApi::class) // Necessário para o FlowRow
@Composable
fun PetInformations(pet: Animal?, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp), // Opcional: manter as bordas arredondadas
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Opcional: adicionar uma leve sombra
    ) {
        if (pet == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PetImage(pet = pet, modifier = Modifier.size(120.dp))

                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // --- SEÇÃO DE TAGS ---
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TagChip para Espécie
                    val speciesIcon = Icons.Default.Pets
                    val speciesText = if (pet.species == "DOG") "Cachorro" else "Gato"
                    TagChip(text = speciesText, icon = speciesIcon,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer)

                    // TagChip para Sexo (usando a lógica do seu SexIcon)
                    val (sexIcon, sexColor) = when (pet.sex) {
                        "MALE" -> Icons.Default.Male to MaterialTheme.colorScheme.primary
                        "FEMALE" -> Icons.Default.Female to MaterialTheme.colorScheme.secondary
                        else -> Icons.Default.HelpOutline to MaterialTheme.colorScheme.outline
                    }
                    val sexText = when (pet.sex) {
                        "MALE" -> "Macho"
                        "FEMALE" -> "Fêmea"
                        else -> "Não informado"
                    }
                    TagChip(
                        text = sexText,
                        icon = sexIcon,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    // TagChip para Castrado
                    val neuteredText =
                        if (pet.neutered == true) "Castrado(a)" else "Não Castrado(a)"
                    val neuteredIcon =
                        if (pet.neutered == true) Icons.Default.Check else Icons.Default.Close
                    TagChip(
                        text = neuteredText,
                        icon = neuteredIcon,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Divider(modifier = Modifier.padding(top = 8.dp))

                // --- SEÇÃO DE DETALHES ---
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoChip(label = "Nascimento", value = pet.birthDate)

                    // Adicionando Peso
                    pet.weight?.let { weight ->
                        InfoChip(label = "Peso", value = "$weight kg")
                    }

                    // Adicionando Microchip (condicional)
                    pet.microchip?.let { microchipNumber ->
                        InfoChip(label = "Microchip", value = microchipNumber ?: "-")
                    }
                }
            }
        }
    }
}


// O InfoChip pode ser simplificado se quisermos que ele use sempre as cores padrão
@Composable
fun InfoChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally // Alinha o texto no centro
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TagChip(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Surface(
        modifier = modifier,
        shape = CircleShape, // Formato de "pílula"
        color = containerColor,
        tonalElevation = 2.dp // Uma leve elevação para destacar
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // O texto já descreve
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )
        }
    }
}