package com.example.carteirapet.screen.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        // Gera o texto transformado com máscara
        val transformedText = buildString {
            for (i in originalText.indices) {
                if (i == 5) append("-") // Adiciona o traço no índice 5
                append(originalText[i])
            }
        }

        // Mapeamento de offsets
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 5) offset else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= 5) offset else offset - 1
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
