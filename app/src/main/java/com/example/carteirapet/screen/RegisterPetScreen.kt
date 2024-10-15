package com.example.carteirapet.screen

import android.net.Uri
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.carteirapet.R
import com.example.carteirapet.ui.theme.CarteiraPetTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPetScreen(backToHomeScreen: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    var petImageUri by remember { mutableStateOf<Uri?>(null) }
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
            ImagePicker(onImageSelected = { petImageUri = it })
            SpeciesSelection { selectedSpecies ->
                // Ação ao selecionar a espécie (pode ser um log ou uma atualização de estado)
                println("Espécie selecionada: $selectedSpecies")
            }

            PetForm({})
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
    onSpeciesSelected: (String) -> Unit
) {
    var selectedSpecies by remember { mutableStateOf<String?>(null) }

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
                        selectedSpecies = "Cachorro"
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
                        selectedSpecies = "Gato"
                        onSpeciesSelected("Gato")
                    }
                )
                Text(text = "Gato")
            }
        }
    }
}

@Composable
fun PetForm(
    onSave: (PetData) -> Unit
) {
    // Estados para armazenar os valores dos campos
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var microchip by remember { mutableStateOf("") }
    var selectedBreed by remember { mutableStateOf("Selecionar raça") }
    var selectedSex by remember { mutableStateOf("Selecionar sexo") }
    var isNeutered by remember { mutableStateOf(false) }
    var conditions by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    val breeds = listOf("Labrador", "Bulldog", "Poodle", "Vira-lata", "Outra")
    val sexes = listOf("Macho", "Fêmea")

    Column(modifier = Modifier.padding(16.dp)) {
        // Campo de Nome
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Data de Nascimento
        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Data de Nascimento") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("DD/MM/AAAA") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Microchip
        OutlinedTextField(
            value = microchip,
            onValueChange = { microchip = it },
            label = { Text("Microchip") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown de Raça
        DropdownMenuField(
            label = "Raça",
            options = breeds,
            selectedOption = selectedBreed,
            onOptionSelected = { selectedBreed = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown de Sexo
        DropdownMenuField(
            label = "Sexo",
            options = sexes,
            selectedOption = selectedSex,
            onOptionSelected = { selectedSex = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox de Castrado
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isNeutered,
                onCheckedChange = { isNeutered = it }
            )
            Text(text = "Castrado")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Condições Pré-existentes
        OutlinedTextField(
            value = conditions,
            onValueChange = { conditions = it },
            label = { Text("Condições Pré-existentes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Peso
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Salvar
        Button(
            onClick = {
                val petData = PetData(
                    name = name,
                    birthDate = birthDate,
                    microchip = microchip,
                    breed = selectedBreed,
                    sex = selectedSex,
                    neutered = isNeutered,
                    conditions = conditions,
                    weight = weight
                )
                onSave(petData)
            },
            modifier = Modifier.align(Alignment.End).fillMaxWidth()
        ) {
            Text(text = "Salvar")
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
            enabled = false, // Desabilitado para evitar edição direta
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(onClick = {
//                    onOptionSelected(option)
//                    expanded = false
//                }) {
//                    Text(text = option)
//                }
//            }
//        }

        // Expande o Dropdown ao clicar no campo
        Spacer(modifier = Modifier
            .matchParentSize()
            .clickable { expanded = true }
        )
    }
}

data class PetData(
    val name: String,
    val birthDate: String,
    val microchip: String,
    val breed: String,
    val sex: String,
    val neutered: Boolean,
    val conditions: String,
    val weight: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CarteiraPetTheme {
        RegisterPetScreen({})
    }
}