package com.example.carteirapet.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.ui.theme.CarteiraPetTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterProfileUserScreen(goToLoginScreen: () -> Unit, goToHomeScreen: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
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
                    IconButton(onClick = { goToLoginScreen() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },

                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(text = "Olá, para continuar, você deve preencher alguns dados referentes ao seu perfil.")

            UserRegistrationForm(goToHomeScreen)
        }
    }
}

@Composable
fun UserRegistrationForm(goToHomeScreen: () -> Unit) {
    var currentStep by remember { mutableStateOf(1) }

    // Step 1: User personal information
    var isVet by remember { mutableStateOf(true) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }

    // Step 2: Address information
    var cep by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var complement by remember { mutableStateOf("") }
    var neighborhood by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(2, currentStep)

        when (currentStep) {
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
                            selected = !isVet,
                            onClick = { isVet = false }
                        )
                        Text("Tutor")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = isVet,
                            onClick = { isVet = true }
                        )
                        Text("Médico Veterinário")


                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Personal information fields
                PersonalInformationForm(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = {lastName = it},
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    email = email,
                    onEmailChange = { email = it },
                    cpf = cpf,
                    onCpfChange = { cpf = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Button to go to step 2
                Button(onClick = { currentStep = 2 }, modifier = Modifier.fillMaxWidth()) {
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
                    cep = cep,
                    onCepChange = { cep = it },
                    street = street,
                    onStreetChange = { street = it },
                    complement = complement,
                    onComplementChange = { complement = it },
                    neighborhood = neighborhood,
                    onNeighborhoodChange = { neighborhood = it },
                    city = city,
                    onCityChange = { city = it },
                    state = state,
                    onStateChange = { state = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Navigation buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { currentStep = 1 }, modifier = Modifier.weight(1f)) {
                        Text("Voltar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { goToHomeScreen() }, modifier = Modifier.weight(1f)) {
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
    cpf: String,
    onCpfChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text("Sobrenome") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = { Text("Celular") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = cpf,
            onValueChange = onCpfChange,
            label = { Text("CPF") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun AddressInformationForm(
    cep: String,
    onCepChange: (String) -> Unit,
    street: String,
    onStreetChange: (String) -> Unit, complement: String,
    onComplementChange: (String) -> Unit,
    neighborhood: String,
    onNeighborhoodChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    state: String,
    onStateChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = cep,
            onValueChange = onCepChange,
            label = { Text("CEP") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = street,
            onValueChange = onStreetChange,
            label = { Text("Rua") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = complement,
            onValueChange = onComplementChange,
            label = { Text("Complemento") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = neighborhood,
            onValueChange = onNeighborhoodChange,
            label = { Text("Bairro") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = city,
            onValueChange = onCityChange,
            label = { Text("Cidade") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state,
            onValueChange = onStateChange,
            label = { Text("Estado") },
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
@Preview(showBackground = true)
fun RegisterProfileUserPreview1() {
    CarteiraPetTheme {
        RegisterProfileUserScreen({}, {})
//        ProgressBar(steps = 2, currentStep = 1)
    }
}
