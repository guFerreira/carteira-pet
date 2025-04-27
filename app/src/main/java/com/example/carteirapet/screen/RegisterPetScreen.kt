package com.example.carteirapet.screen

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.widget.DatePicker
import android.widget.Space
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.carteirapet.screen.components.DateFieldInput
import com.example.carteirapet.screen.components.ImagePicker
import com.example.carteirapet.screen.components.NumberFieldInput
import com.example.carteirapet.screen.components.SpeciesSelection
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.RegisterPetViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPetScreen(
    backToHomeScreen: () -> Unit,
    viewModel: RegisterPetViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { viewModel.loadBreeds() }


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
                        "Registrar Pet \uD83D\uDC3E",
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = backToHomeScreen) {
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

            if (viewModel.breedOptions.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Carregando raças...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                ImagePicker(onImageSelected = { viewModel.petImageByteArray =
                    it?.let { it1 -> readImageAsByteArray(it1, context) }
                })

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Campos marcados com * são obrigatórios",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Seção: Informações básicas
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Informações Básicas",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.name,
                                onValueChange = {
                                    viewModel.name = it
//                                    viewModel.clearNameError()
                                },
                                label = { Text("Nome *") },
//                                isError = viewModel.nameError != null,
                                supportingText = { Text("Nome do seu pet") },
                                modifier = Modifier
                                    .fillMaxWidth()
//                                    .semantics { contentDescription = "Nome do pet, obrigatório" }
                            )
//                            viewModel.nameError?.let {
//                                Text(
//                                    text = it,
//                                    color = MaterialTheme.colorScheme.error,
//                                    style = MaterialTheme.typography.bodySmall,
//                                    modifier = Modifier.padding(start = 4.dp)
//                                )
//                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            SpeciesSelection(
                                selectedSpecies = viewModel.species,
                                onSpeciesSelected = {
                                    viewModel.changeSpecies(it)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Seção: Características
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Características",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            DateFieldInput(
                                inputName = "Data de Nascimento *",
                                value = viewModel.birthDate,
                                onDateSelected = { viewModel.birthDate = it }
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            DropdownMenuField(
                                label = "Raça *",
                                options = viewModel.breedOptions,
                                selectedOption = viewModel.selectedBreed,
                                onOptionSelected = { viewModel.selectedBreed = it },
                                displayText = { it.name }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            DropdownMenuField(
                                label = "Sexo *",
                                options = viewModel.sexes,
                                selectedOption = viewModel.sex,
                                onOptionSelected = { viewModel.sex = it },
                                displayText = { it }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = viewModel.neutered,
                                    onCheckedChange = { viewModel.neutered = it }
                                )
                                Text(text = "Castrado *")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            NumberFieldInput(
                                inputName = "Peso (kg) *",
                                value = viewModel.weight,
                                onValueChange =  {
                                    viewModel.weight = it
//                                    viewModel.clearWeightError()
                                },
                                enabled = true,
                                supportingText = "Peso em kg (ex.: 15.5)",
                                modifier = Modifier
                                    .semantics { contentDescription = "Peso do pet, obrigatório" }
                            )
//                            viewModel.weightError?.let {
//                                Text(
//                                    text = it,
//                                    color = MaterialTheme.colorScheme.error,
//                                    style = MaterialTheme.typography.bodySmall,
//                                    modifier = Modifier.padding(start = 4.dp)
//                                )
//                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Seção: Detalhes adicionais
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Detalhes Adicionais",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.microchip,
                                onValueChange = { viewModel.microchip = it },
                                label = { Text("Microchip") },
                                supportingText = { Text("Número do microchip, se disponível") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = viewModel.conditions,
                                onValueChange = { viewModel.conditions = it },
                                label = { Text("Condições Pré-existentes") },
                                supportingText = { Text("Ex.: alergias, doenças") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Botão de Salvar
                    Button(
                        onClick = {
                            viewModel.registerPet(
                                backToHomeScreen,
                                { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                })
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Salvar")
                    }
                }
            }
        }
    }

}

fun readImageAsByteArray(uri: Uri, context: Context): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
}



@Composable
fun <T> DropdownMenuField(
    label: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    displayText: (T) -> String,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            enabled = enabled,
            value = selectedOption?.let { displayText(it) } ?: "",
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(displayText(option)) }
                )
            }
        }

        // Expande o Dropdown ao clicar no campo
        Spacer(modifier = Modifier
            .matchParentSize()
            .clickable { expanded = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CarteiraPetTheme {
        RegisterPetScreen({})
    }
}