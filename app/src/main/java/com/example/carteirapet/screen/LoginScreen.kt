package com.example.carteirapet.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.R
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.LoginState
import com.example.carteirapet.viewModels.LoginViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(onSignUpClick: () -> Unit, onLoginSuccess: () -> Unit, viewModel: LoginViewModel = koinViewModel()) {
    val loginState = viewModel.loginState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(180.dp)

            )
            Text(text = "A sua carteira de vacinas digital", fontSize = 12.sp)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation() // Para ocultar o texto
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Button(
                onClick = { viewModel.login(onLoginSuccess)},
                enabled = viewModel.isLoginEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            ElevatedButton(
                onClick = { onSignUpClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Cor de fundo baseada no tema
                    contentColor = MaterialTheme.colorScheme.onSecondary // Cor do conteúdo (texto/ícone) baseada no tema
                )
            ) {
                Text("Criar conta")
            }
        }

        // Observa o estado do login e exibe a UI apropriada
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginState.Success -> {
                Text(text = (loginState as LoginState.Success).message)
            }
            is LoginState.Error -> {
                Text(text = (loginState as LoginState.Error).error, color = MaterialTheme.colorScheme.error)
            }
            else -> {}
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    CarteiraPetTheme {
        LoginScreen({}, {}, )
    }
}

