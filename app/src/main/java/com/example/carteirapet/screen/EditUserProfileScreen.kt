package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    goToHomeScreen: () -> Unit,
    viewModel: EditUserProfileViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.isLoading = true
        viewModel.loadUserProfile { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.isLoading = false
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(
                        "Editar perfil",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                navigationIcon = {
                    IconButton(onClick = goToHomeScreen) {
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxWidth()
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.isLoading){
                CircularProgressIndicator()
            } else{
                // Personal information fields
                PersonalInformationForm(
                    firstName = viewModel.firstName,
                    onFirstNameChange = { viewModel.firstName = it },
                    lastName = viewModel.lastName,
                    onLastNameChange = { viewModel.lastName = it },
                    phoneNumber = viewModel.phoneNumber,
                    onPhoneNumberChange = { viewModel.phoneNumber = it },
                    email = viewModel.email,
                    onEmailChange = { viewModel.email = it },
                    cpf = viewModel.cpf,
                    onCpfChange = { viewModel.cpf = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AddressInformationForm(
                    cep = viewModel.cep,
                    onCepChange = { viewModel.cep = it },
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
                Button(
                    onClick = {
                        viewModel.updateProfileData(
                            goToHomeScreen,
                            onError = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            })
                    },
                ) {
                    Text("Concluir")
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