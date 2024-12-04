package com.example.carteirapet.screen.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // Gera o texto transformado com máscara
        val transformedText = buildString {
            for (i in originalText.indices) {
                when (i) {
                    3, 6 -> append(".") // Adiciona ponto
                    9 -> append("-")   // Adiciona traço
                }
                append(originalText[i])
            }
        }

        // Mapeamento de offsets
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset // Antes do primeiro ponto
                    offset in 4..6 -> offset + 1 // Entre primeiro e segundo ponto
                    offset in 7..9 -> offset + 2 // Entre segundo ponto e traço
                    offset > 9 -> offset + 3 // Após o traço
                    else -> transformedText.length
                }.coerceAtMost(transformedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset // Antes do primeiro ponto
                    offset in 4..7 -> offset - 1 // Após o primeiro ponto
                    offset in 8..10 -> offset - 2 // Após o segundo ponto
                    offset > 10 -> offset - 3 // Após o traço
                    else -> originalText.length
                }.coerceIn(0, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
