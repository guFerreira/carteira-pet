package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.screen.components.CepVisualTransformation
import com.example.carteirapet.screen.components.CpfVisualTransformation
import com.example.carteirapet.screen.components.PhoneVisualTransformation
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.RegisterProfileUserViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterProfileUserScreen(
    goToLoginScreen: () -> Unit,
    goToHomeScreen: (userType: Int) -> Unit,
    viewModel: RegisterProfileUserViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Finalize o seu cadastro",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout(
                            goToLoginScreen
                        ) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Logout, // Ícone de Logout
                            contentDescription = "Sair da conta" // Descrição clara para acessibilidade
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                UserRegistrationForm({ goToHomeScreen(if (viewModel.isVet) 1 else 0) }, viewModel)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRegistrationForm(goToHomeScreen: () -> Unit, viewModel: RegisterProfileUserViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(2, viewModel.currentStep)

        when (viewModel.currentStep) {
            1 -> {
                Text(
                    "Etapa 1: Informações Pessoais",
                    style = MaterialTheme.typography.titleLarge, // Título da seção
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp) // Espaçamento e padding
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Você é um:",
                        style = MaterialTheme.typography.bodyMedium, // Estilo de texto para rótulos
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // RadioButton e Text juntos para acessibilidade e toque.
                        // O M3 encoraja o uso de 'selectable' modificador em Box/Surface
                        // para grupos de seleção, mas para pares RadioButton+Text, assim é ok.
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            Row(
                                modifier = Modifier
                                    .selectable(
                                        selected = !viewModel.isVet,
                                        onClick = { viewModel.updateIsVet(false) },
                                        role = Role.RadioButton
                                    )
                                    .padding(end = 8.dp), // Ajuste de padding
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = !viewModel.isVet,
                                    onClick = null // onClick no selectable
                                )
                                Text(
                                    "Tutor",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(
                                modifier = Modifier
                                    .selectable(
                                        selected = viewModel.isVet,
                                        onClick = { viewModel.updateIsVet(true) },
                                        role = Role.RadioButton
                                    )
                                    .padding(start = 8.dp), // Ajuste de padding
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = viewModel.isVet,
                                    onClick = null // onClick no selectable
                                )
                                Text(
                                    "Médico Veterinário",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Aumentando espaçamento

                // Personal information fields
                PersonalInformationForm(
                    firstName = viewModel.firstName,
                    onFirstNameChange = viewModel::updateFirstName,
                    lastName = viewModel.lastName,
                    onLastNameChange = viewModel::updateLastName,
                    phoneNumber = viewModel.phoneNumber,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                    email = viewModel.email,
                    onEmailChange = viewModel::updateEmail,
                    cpf = viewModel.cpf,
                    onCpfChange = viewModel::updateCpf
                )
                if (viewModel.isVet) {
                    Spacer(modifier = Modifier.height(16.dp)) // Consistência no espaçamento
                    OutlinedTextField(
                        value = viewModel.crmv,
                        onValueChange = viewModel::updateCrmv,
                        label = { Text("CRMV") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp)) // Mais espaçamento antes do botão

                // Button to go to step 2
                Button(
                    onClick = { viewModel.goToNextStep() },
                    enabled = viewModel.validateRequiredFieldsInFirstStep(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Altura padrão para botões M3
                ) {
                    Text("Próximo", style = MaterialTheme.typography.titleMedium) // Estilo de texto do botão
                }
            }

            2 -> {
                Text(
                    "Etapa 2: Dados de Endereço",
                    style = MaterialTheme.typography.titleLarge, // Consistência com o título da Etapa 1
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp)) // Mais espaçamento antes dos botões
                AddressInformationForm(
                    isSearchingCep = viewModel.isSearchingCep,
                    cep = viewModel.cep,
                    onCepChange = viewModel::updateCep,
                    street = viewModel.street,
                    onStreetChange = viewModel::updateStreet,
                    number = viewModel.number,
                    onNumberChange = viewModel::updateNumber,
                    complement = viewModel.complement,
                    onComplementChange = viewModel::updateComplement,
                    city = viewModel.city,
                    onCityChange = viewModel::updateCity,
                    state = viewModel.state,
                    onStateChange = viewModel::updateState
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Usando OutlinedButton para ações secundárias, como "Voltar"
                    OutlinedButton(
                        onClick = { viewModel.goToPreviousStep() },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text("Voltar", style = MaterialTheme.typography.titleMedium)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        enabled = viewModel.validateRequiredFieldsInSecondStep(),
                        onClick = {
                            viewModel.registerProfileData(
                                goToHomeScreen,
                                onError = { message ->
                                    Toast.makeText(
                                        context,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        Text("Concluir", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalInformationForm(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    enableEmail: Boolean = true,
    cpf: String,
    onCpfChange: (String) -> Unit,
    enableCpf: Boolean = true,
) {
    Column {
        OutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text("Nome *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true // Campos de texto de linha única para melhor UX
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaçamento consistente
        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Sobrenome *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        PhoneInput(phoneNumber, onPhoneNumberChange)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            enabled = enableEmail,
            label = { Text("Email *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            enabled = enableCpf,
            value = cpf,
            onValueChange = {
                if (it.length <= 11) {
                    onCpfChange(it)
                }
            },
            label = { Text("CPF *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CpfVisualTransformation(),
            singleLine = true
        )
    }
}

@Composable
fun AddressInformationForm(
    isSearchingCep: Boolean = false,
    cep: String,
    onCepChange: (String) -> Unit,
    street: String,
    onStreetChange: (String) -> Unit,
    number: String,
    onNumberChange: (String) -> Unit,
    complement: String,
    onComplementChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    state: String,
    onStateChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = cep,
            onValueChange = {
                if (it.length <= 8) {
                    onCepChange(it)
                }
            },
            label = { Text("CEP *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CepVisualTransformation(),
            singleLine = true
        )
        if (isSearchingCep){
            // Usando LinearProgressIndicator para indicar busca de CEP
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Espaçamento adequado
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Buscando CEP...",
                style = MaterialTheme.typography.bodySmall, // Texto de dica
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = street,
            onValueChange = onStreetChange,
            label = { Text("Rua *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            label = { Text("Cidade *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = state,
            onValueChange = onStateChange,
            label = { Text("Estado *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = number,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onNumberChange(newValue)
                }
            },
            label = { Text("Número *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = complement,
            onValueChange = onComplementChange,
            label = { Text("Complemento") }, // Complemento geralmente não é obrigatório
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Composable
fun ProgressBar(steps: Int, currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp), // Ajustando padding vertical
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until steps) {
            StepCircle(isActive = i < currentStep, step = i + 1)

            if (i < steps - 1) {
                // Usando LinearProgressIndicator para a linha, que se alinha melhor com M3
                val progress = if (i < currentStep - 1) 1f else 0f
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant // Cor de fundo para a "trilha"
                )
            }
        }
    }
}


@Composable
fun StepCircle(isActive: Boolean, step: Int) {
    // Cor de fundo da bolinha
    val backgroundColor = if (isActive) {
        MaterialTheme.colorScheme.primary // Primário para ativo
    } else {
        // Usamos surfaceContainerHighest para um fundo inativo mais escuro e com melhor contraste
        MaterialTheme.colorScheme.surfaceContainerHighest
    }

    // Cor do texto
    val textColor = if (isActive) {
        MaterialTheme.colorScheme.onPrimary // Texto claro para bolinha primária
    } else {
        // Usamos onSurface para garantir bom contraste com surfaceContainerHighest
        MaterialTheme.colorScheme.onSurface
    }

    // Cor da borda (opcional, mas pode ajudar a definir o limite do círculo inativo)
    val borderColor = if (isActive) {
        MaterialTheme.colorScheme.primary // Borda com a mesma cor primária para ativo
    } else {
        // Usamos outline ou outlineVariant para a borda do inativo
        MaterialTheme.colorScheme.outline
    }

    Box(
        modifier = Modifier
            .size(32.dp) // Tamanho um pouco maior para melhor toque e visual
            .clip(CircleShape)
            .background(backgroundColor) // Aplica a cor de fundo
            .border(2.dp, borderColor, CircleShape), // Adiciona uma borda
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$step",
            color = textColor,
            style = MaterialTheme.typography.labelLarge // Estilo de texto M3 para rótulos pequenos
        )
    }
}

@Composable
fun PhoneInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {
    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { newValue ->
            if (newValue.length <= 11) {
                onPhoneNumberChange(newValue.filter { it.isDigit() })
            }
        },
        label = { Text("Celular *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation(),
        singleLine = true // Adicionando singleLine
    )
}

@Composable
@Preview(showBackground = true)
fun RegisterProfileUserPreview1() {
    CarteiraPetTheme {
        RegisterProfileUserScreen({}, {})
//        ProgressBar(steps = 2, currentStep = 1)
    }
}

