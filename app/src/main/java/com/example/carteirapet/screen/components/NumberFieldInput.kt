package com.example.carteirapet.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun NumberFieldInput(
    inputName: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    supportingText: String,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Filtra apenas números, ponto decimal e sinal de menos
            if (newValue.isValidDecimal()) {
                onValueChange(newValue)
            }
        },
        label = { Text(inputName) },
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal, // Teclado para números decimais
            imeAction = ImeAction.Done,
            autoCorrect = false // Desativa autocorreção
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() } // Fecha o teclado ao pressionar "Done"
        ),
        singleLine = true, // Impede novas linhas
        placeholder = { Text("Ex: 5.2") },
        supportingText = { Text(supportingText) }
    )
}

// Função de validação para números decimais
private fun String.isValidDecimal(): Boolean {
    // Permite vazio (para apagar), números, um ponto decimal e um sinal de menos no início
    // Bloqueia vírgula, espaço, nova linha e outros caracteres
    return this.isEmpty() || this.matches(Regex("^-?\\d*\\.?\\d*$"))
}