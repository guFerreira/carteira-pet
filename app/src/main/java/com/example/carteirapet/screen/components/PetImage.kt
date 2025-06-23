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
fun PetImage(pet: Animal, modifier: Modifier = Modifier) { // Modificador externo para controle de tamanho e borda
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
            contentDescription = "Foto do pet ${pet.name}", // Descrição mais específica
            modifier = modifier // Aplica o modificador passado, que incluirá o size e border
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer), // Cor de fundo para caso a imagem demore a carregar
            contentScale = ContentScale.Crop // Faz a imagem se ajustar ao formato circular
        )
    } else {
        // Fallback com Box e emoji
        Box(
            modifier = modifier // Aplica o modificador passado para size e border
                .clip(CircleShape) // Garante o clipe circular
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer, // Cor de fundo consistente com o tema
                )
                .border(
                    1.dp, // Borda padrão para o fallback
                    MaterialTheme.colorScheme.outlineVariant, // Cor da borda
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (pet.species == "DOG") "🐶" else "😺",
                fontSize = 48.sp, // Tamanho fixo para o emoji, ajustado para caber bem no círculo
                color = MaterialTheme.colorScheme.onSecondaryContainer // Cor do texto/emoji consistente
            )
        }
    }
}