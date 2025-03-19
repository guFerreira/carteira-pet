package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Finalize o seu cadastro",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.logout(
                            goToLoginScreen,
                            { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            })
                    }) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Text(text = "Olá, para continuar, você deve preencher alguns dados referentes ao seu perfil.")
            }
            item {
                UserRegistrationForm({ goToHomeScreen(if (viewModel.isVet) 1 else 0) }, viewModel)
            }
        }
    }
}


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
                // Step 1: Personal information
                Text(
                    "Etapa 1: Informações Pessoais",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Radio buttons for user type
                    Text(text = "Você é um:")
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        RadioButton(
                            selected = !viewModel.isVet,
                            onClick = { viewModel.updateIsVet(false) }
                        )
                        Text("Tutor")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = viewModel.isVet,
                            onClick = { viewModel.updateIsVet(true) }
                        )
                        Text("Médico Veterinário")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.crmv,
                        onValueChange = viewModel::updateCrmv,
                        label = { Text("CRMV") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to go to step 2
                Button(
                    onClick = { viewModel.goToNextStep() },
                    enabled = viewModel.validateRequiredFieldsInFirstStep(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Próximo")
                }
            }

            2 -> {
                // Step 2: Address information
                Text(
                    "Etapa 2: Dados de Endereço",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
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

                // Navigation buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.goToPreviousStep() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Voltar")
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Concluir")
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
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Sobrenome *") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PhoneInput(phoneNumber, onPhoneNumberChange)

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            enabled = enableEmail,
            label = { Text("Email *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            enabled = enableCpf,
            value = cpf,
            onValueChange = {
                if (it.length <= 11) { // Limitar a entrada a 11 caracteres
                    onCpfChange(it)
                }
            },
            label = { Text("CPF *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CpfVisualTransformation()
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
                if (it.length <= 8) { // Limitar a entrada a 8 caracteres (formato numérico do CEP)
                    onCepChange(it)
                }
            },
            label = { Text("CEP *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CepVisualTransformation()
        )
        if (isSearchingCep){
            Text(text = "Buscando cep...")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = street,
            onValueChange = onStreetChange,
            label = { Text("Rua *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            label = { Text("Cidade *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state,
            onValueChange = onStateChange,
            label = { Text("Estado *") },
            enabled = !isSearchingCep,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = number,
            onValueChange = { newValue ->
                // Permite apenas números inteiros
                if (newValue.all { it.isDigit() }) {
                    onNumberChange(newValue)
                }
            },
            label = { Text("Número *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = complement,
            onValueChange = onComplementChange,
            label = { Text("Complemento *") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProgressBar(steps: Int, currentStep: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until steps) {
            StepCircle(isActive = i < currentStep, i + 1)

            if (i < steps - 1) {
                val color =
                    if (i < currentStep) Color(MaterialTheme.colorScheme.primary.value) else Color.Gray
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .weight(1f) // Preencher o espaço disponível
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun StepCircle(isActive: Boolean, step: Int) {
    val color = if (isActive) Color(MaterialTheme.colorScheme.primary.value) else Color.Gray
    val textColor =
        if (isActive) Color(MaterialTheme.colorScheme.onPrimary.value) else Color.DarkGray
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$step", color = textColor)
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
            // Limita o número de caracteres digitados
            if (newValue.length <= 11) {
                onPhoneNumberChange(newValue.filter { it.isDigit() }) // Aceita apenas números
            }
        },
        label = { Text("Celular *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation()
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

