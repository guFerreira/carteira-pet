package com.example.carteirapet.screen

import android.net.Uri
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.RegisterPetViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPetScreen(backToHomeScreen: () -> Unit, viewModel: RegisterPetViewModel = koinViewModel()) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()

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
                        "Registrar Pet",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                },
                navigationIcon = {
                    IconButton(onClick = backToHomeScreen) {
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
                .fillMaxWidth().verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagePicker(onImageSelected = { viewModel.petImageUri = it })
            SpeciesSelection(viewModel.species, { viewModel.species = it })

//            PetForm(viewModel, backToHomeScreen)

            Column(modifier = Modifier.padding(16.dp)) {
                // Campo de Nome
                OutlinedTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Data de Nascimento
                OutlinedTextField(
                    value = viewModel.birthDate,
                    onValueChange = { viewModel.birthDate = it },
                    label = { Text("Data de Nascimento") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("DD/MM/AAAA") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Microchip
                OutlinedTextField(
                    value = viewModel.microchip,
                    onValueChange = { viewModel.microchip = it },
                    label = { Text("Microchip") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown de Raça
                DropdownMenuField(
                    label = "Raça",
                    options = viewModel.breeds,
                    selectedOption = viewModel.breed,
                    onOptionSelected = { viewModel.breed = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown de Sexo
                DropdownMenuField(
                    label = "Sexo",
                    options = viewModel.sexes,
                    selectedOption = viewModel.sex,
                    onOptionSelected = { viewModel.sex = it}
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Checkbox de Castrado
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.neutered,
                        onCheckedChange = { viewModel.neutered = it }
                    )
                    Text(text = "Castrado")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Condições Pré-existentes
                OutlinedTextField(
                    value = viewModel.conditions,
                    onValueChange = { viewModel.conditions = it },
                    label = { Text("Condições Pré-existentes") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de Peso
                OutlinedTextField(
                    value = viewModel.weight,
                    onValueChange = { viewModel.weight = it },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão de Salvar
                Button(
                    onClick = {
                        viewModel.registerPet(backToHomeScreen, { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() })
                    },
                    modifier = Modifier.align(Alignment.End).fillMaxWidth()
                ) {
                    Text(text = "Salvar")
                }
            }
        }
    }
}


@Composable
fun ImagePicker(
    onImageSelected: (Uri?) -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        onImageSelected(uri)
    }

    Card(
        onClick = { launcher.launch("image/*") },
        modifier = Modifier
            .padding(12.dp) // Primeiro aplica padding
            .clip(RoundedCornerShape(360.dp)) // Aplica o arredondamento
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .width(120.dp)
            .height(120.dp)
    ) {
        // Box para centralizar o conteúdo
        Box(
            contentAlignment = Alignment.Center, // Centraliza o conteúdo
            modifier = Modifier.fillMaxSize() // Ocupa todo o espaço do Card
        ) {
            if (selectedImageUri == null) {
                Icon(
                    Icons.Filled.AddAPhoto,
                    contentDescription = "Selecionar Imagem",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer // Altera a cor do ícone
                )
            } else {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Imagem selecionada",
                    contentScale = ContentScale.Crop, // Faz com que a imagem ocupe todo o espaço
                    modifier = Modifier
                        .fillMaxSize() // Garante que a imagem ocupe todo o Card
                        .clip(CircleShape) // Aplica o formato circular
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onPrimaryContainer,
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun SpeciesSelection(
    selectedSpecies: String,
    onSpeciesSelected: (String) -> Unit
) {
    Column {
        Text(text = "Espécie:")
        Row {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedSpecies == "Cachorro",
                    onClick = {
                        onSpeciesSelected("Cachorro")
                    }
                )
                Text(text = "Cachorro")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                RadioButton(
                    selected = selectedSpecies == "Gato",
                    onClick = {
                        onSpeciesSelected("Gato")
                    }
                )
                Text(text = "Gato")
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            enabled = true, // Desabilitado para evitar edição direta
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
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