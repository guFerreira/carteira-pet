package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.EditUserProfileViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUserProfileScreen(
    goToHomeScreen: (userType: Int) -> Unit,
    viewModel: EditUserProfileViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        "Editar perfil",
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (viewModel.isVet) goToHomeScreen(1) else goToHomeScreen(0)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = MaterialTheme.colorScheme.onSurface
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
                .safeContentPadding()
                .fillMaxWidth()
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Aqui você pode visualizar e editar as informações do seu perfil ☺\uFE0F")

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Informações pessoais",
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                // Personal information fields
                PersonalInformationForm(
                    firstName = viewModel.firstName,
                    onFirstNameChange = { viewModel.firstName = it },
                    lastName = viewModel.lastName,
                    onLastNameChange = { viewModel.lastName = it },
                    phoneNumber = viewModel.phoneNumber,
                    onPhoneNumberChange = { viewModel.phoneNumber = it },
                    email = viewModel.email,
                    enableEmail = false,
                    onEmailChange = { viewModel.email = it },
                    cpf = viewModel.cpf,
                    onCpfChange = { viewModel.cpf = it },
                    enableCpf = false
                )
                if (viewModel.isVet) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.crmv,
                        onValueChange = { viewModel.crmv = it },
                        label = { Text("CRMV *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Endereço",
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                AddressInformationForm(
                    isSearchingCep = viewModel.isSearchingCep,
                    cep = viewModel.cep,
                    onCepChange = { viewModel.updateCep(it) },
                    street = viewModel.street,
                    onStreetChange = { viewModel.street = it },
                    number = viewModel.number,
                    onNumberChange = { viewModel.number = it },
                    complement = viewModel.complement,
                    onComplementChange = { viewModel.complement = it },
                    city = viewModel.city,
                    onCityChange = { viewModel.city = it },
                    state = viewModel.state,
                    onStateChange = { viewModel.state = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    enabled = viewModel.validateRequiredPersonInformations() && viewModel.validateRequiredAddressInformations(),
                    onClick = {
                        viewModel.updateProfileData(
                            { if (viewModel.isVet) goToHomeScreen(1) else goToHomeScreen(0) },
                            onError = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            })
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                        disabledContentColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar")
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditUserProfileScreenPreview() {
    CarteiraPetTheme {
        EditUserProfileScreen({})
    }
}