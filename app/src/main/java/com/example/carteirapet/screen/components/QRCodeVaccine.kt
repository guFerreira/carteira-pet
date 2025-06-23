package com.example.carteirapet.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import qrgenerator.qrkitpainter.PatternType
import qrgenerator.qrkitpainter.PixelType.SquarePixel
import qrgenerator.qrkitpainter.QrBallType
import qrgenerator.qrkitpainter.QrFrameType
import qrgenerator.qrkitpainter.QrKitBrush
import qrgenerator.qrkitpainter.getSelectedPattern
import qrgenerator.qrkitpainter.getSelectedPixel
import qrgenerator.qrkitpainter.getSelectedQrBall
import qrgenerator.qrkitpainter.getSelectedQrFrame
import qrgenerator.qrkitpainter.rememberQrKitPainter
import qrgenerator.qrkitpainter.solidBrush

@Composable
fun QRCodeVaccine(inputText: String) {
    // RECOMENDAÇÃO: Use onSurface para garantir o melhor contraste em qualquer tema.
    val qrCodeColor = MaterialTheme.colorScheme.tertiary

    val painter = rememberQrKitPainter(
        data = inputText,
        options = {
            qrColors {
                // Aplica a cor escolhida aos elementos do QR code
                darkColorBrush = QrKitBrush.solidBrush(qrCodeColor)
                frameColorBrush = QrKitBrush.solidBrush(qrCodeColor)
            }
            qrShapes {
                // Suas configurações de forma
                ballShape = getSelectedQrBall(QrBallType.SquareQrBall())
                darkPixelShape = getSelectedPixel(SquarePixel())
                frameShape = getSelectedQrFrame(QrFrameType.SquareQrFrame())
                qrCodePattern = getSelectedPattern(PatternType.SquarePattern)
            }
        }
    )

    Image(
        painter = painter,
        contentDescription = "QR Code para a solicitação de vacina", // Adicionado contentDescription
        modifier = Modifier.size(180.dp)
    )
}