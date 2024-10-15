package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.R
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.PixelType.SquarePixel
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.customBrush
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.getSelectedQrFrame
import qrgenerator.qrkitpainter.rememberQrKitPainter
import qrgenerator.qrkitpainter.solidBrush


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVaccineScreen(goToVaccineCardScreen: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Vaccines,
                            contentDescription = "Nova Vacina",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Nova Vacina",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                },
                navigationIcon = {
                    IconButton(onClick = goToVaccineCardScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Para a sua segurança e de seu pet, uma vacina só pode ser registrada por seu médico veterinário")
            Spacer(modifier = Modifier.size(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(32.dp)
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QR Code",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight(800)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    QRCodeVaccine(inputText = "google.com")
                }
            }

            Spacer(modifier = Modifier.size(8.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Peça para seu médico veterinário escanear o QR Code ou envie o link abaixo para o mesmo")
                CopyTextButton()

            }
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .fillMaxWidth(),
                    onClick = goToVaccineCardScreen) {
                    Text(text = "Concluir")
                }
            }

        }
    }
}

@Composable
fun CopyTextButton() {
    // Obter o ClipboardManager
    val clipboardManager = LocalClipboardManager.current
    // Obter o contexto para exibir um Toast
    val context = LocalContext.current

    Box {
        // Texto que será copiado
        val textToCopy = "Este é o texto que será copiado"

        // Botão que, ao ser clicado, copia o texto
        Button(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary),
            onClick = {
                // Copia o texto para o clipboard
                clipboardManager.setText(AnnotatedString(textToCopy))

                // Exibe um Toast confirmando a cópia
                Toast.makeText(context, "Texto copiado!", Toast.LENGTH_SHORT).show()
            }) {
            Icon(
                Icons.Filled.ContentCopy,
                contentDescription = "Selecionar Imagem",
                tint = MaterialTheme.colorScheme.onPrimary // Altera a cor do ícone
            )
            Text(text = "Copiar o link")
        }

    }
}

@Composable
fun QRCodeVaccine(inputText: String) {
    val centerLogo = painterResource(R.drawable.logo_gato)
    val onTertiaryContainerColor = MaterialTheme.colorScheme.onTertiaryContainer
    val painter = rememberQrKitPainter(
        data = inputText,
        options = {
            centerLogo { painter = centerLogo }

            qrColors {
                darkColorBrush =  QrKitBrush.solidBrush(onTertiaryContainerColor)
//                darkColorBrush = QrKitBrush.customBrush {
//                    Brush.linearGradient(
//                        0f to onTertiaryColor,
//                        1f to onTertiaryColor,
//                        end = Offset(it, it)
//                    )

//                }
                frameColorBrush = QrKitBrush.solidBrush(onTertiaryContainerColor)
            }

            qrShapes {
                ballShape = getSelectedQrBall(QrBallType.SquareQrBall())
                darkPixelShape = getSelectedPixel(SquarePixel())
                frameShape = getSelectedQrFrame(QrFrameType.SquareQrFrame())
                qrCodePattern = getSelectedPattern(PatternType.SquarePattern)
            }
        }
    )

    Image(
        painter = painter, contentDescription = null, modifier = Modifier.size(240.dp)
    )

}


@Composable
@Preview
fun RegisterVaccineScreenPreview() {
    RegisterVaccineScreen({})
}