package com.example.carteirapet.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.carteirapet.R
import com.example.carteirapet.repositories.Animal


@Composable
fun PetImage(pet: Animal, isFromPetInformation: Boolean = false) {
    // Carregar a imagem da URL
    val painter = rememberImagePainter(
        pet.photo, // A URL da imagem
        builder = {
            crossfade(true) // Faz a transição suave entre a imagem
            error(R.drawable.logo_gato) // Imagem de erro, caso a URL seja inválida
        }
    )

    if (pet.photo != null) {
        // Exibir a imagem com a borda
        Image(
            painter = painter,
            contentDescription = "Imagem do pet",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(
                    if (isFromPetInformation) 3.dp else 1.dp,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    CircleShape
                ),
            contentScale = ContentScale.Crop // Faz a imagem se ajustar ao formato circular
        )
    } else {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shape = CircleShape
                ) // Adiciona a cor de fundo
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    CircleShape
                )
                .size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (pet.species == "dog") "🐶" else "😺",
                fontSize = if (isFromPetInformation) 80.sp else 44.sp
            )
        }
    }
}