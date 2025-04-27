package com.example.carteirapet.screen.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ImagePicker(
    onImageSelected: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        onImageSelected(uri)
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = { launcher.launch("image/*") },
            modifier = modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(360.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .size(160.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Adiciona sombra
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (selectedImageUri == null) {
                    Icon(
                        Icons.Filled.AddAPhoto,
                        contentDescription = "Selecionar Imagem",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Column {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Imagem selecionada",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }

        if (selectedImageUri != null) {
            // Botão de remoção
            Button(
                onClick = {
                    selectedImageUri = null
                    onImageSelected(null)
                },
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.surface
                ),
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Remover Imagem",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(text = "Remover", color = MaterialTheme.colorScheme.onErrorContainer)

            }
        }
    }
}