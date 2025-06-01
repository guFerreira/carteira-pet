package com.example.carteirapet.screen.components

import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity

@Composable
fun ShareLinkButton(
    url: String,
    buttonText: String = "Compartilhar Link", // Texto padrão do botão
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Button(
        onClick = {
            // Cria a Intent de compartilhamento
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, url)
                type = "text/plain"
            }

            // Inicia o compartilhamento com um chooser
            startActivity(context, Intent.createChooser(shareIntent, "Compartilhar via"), null)
        },
        modifier = modifier.padding(8.dp) // Adiciona padding para melhor aparência
    ) {
        Icon(
            Icons.Filled.Share,
            contentDescription = "compartilhar",
            tint = MaterialTheme.colorScheme.onPrimary // Altera a cor do ícone
        )
        Text(text = buttonText)
    }
}