package com.example.carteirapet.screen.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // Gera o texto transformado com máscara
        val transformedText = buildString {
            if (originalText.isNotEmpty()) append("(")
            for (i in originalText.indices) {
                when (i) {
                    2 -> append(") ")
                    7 -> append("-")
                }
                append(originalText[i])
            }
        }

        // Calcula o mapeamento de offsets
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset + 1 // Adiciona "("
                    offset in 3..6 -> offset + 3 // Adiciona ") "
                    offset in 7..10 -> offset + 4 // Adiciona "-"
                    else -> transformedText.length
                }.coerceAtMost(transformedText.length) // Limita ao tamanho do transformado
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 1 -> 0 // Antes de "(" retorna índice inicial
                    offset in 2..5 -> offset - 2 // Após ") " ajusta
                    offset in 6..9 -> offset - 3 // Após "-" ajusta
                    offset >= transformedText.length -> originalText.length // Final do texto
                    else -> offset - 4 // Ajuste para máscara final
                }.coerceIn(0, originalText.length) // Limita ao tamanho do original
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
