package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.screen.components.CardUser
import com.example.carteirapet.screen.components.Logo
import com.example.carteirapet.screen.components.PetImage
import com.example.carteirapet.screen.components.PullToRefreshBox
import com.example.carteirapet.ui.theme.CarteiraPetTheme
import com.example.carteirapet.viewModels.MyPetsViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen(
    goToRegisterPetScreen: () -> Unit,
    goToEditUserProfileScreen: () -> Unit,
    goToLoginScreen: () -> Unit,
    goToPetInformation: (petId: Int) -> Unit,
    viewModel: MyPetsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile { message ->
            Toast.makeText(
                context, message, Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.loadAnimals { message ->
            Toast.makeText(
                context, message, Toast.LENGTH_SHORT
            ).show()
        }
    }


    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            title = {
                Logo()
            },
            actions = {
                IconButton(onClick = {goToEditUserProfileScreen()}) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = "Perfil do usuário",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {
                    viewModel.logout(goToLoginScreen, onError = { message ->
                        Toast.makeText(
                            context, message, Toast.LENGTH_SHORT
                        ).show()
                    })
                }) {
                    Icon(
                        Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Sair",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = goToRegisterPetScreen,
            icon = { Icon(Icons.Filled.Add, "Registrar novo pet") },
            text = { Text(text = "Registrar Novo Pet") },
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CardUser(viewModel.name, false)
            PullToRefreshBox(isRefreshing = viewModel.isLoadingPets, onRefresh = {
                viewModel.loadAnimals { message ->
                    Toast.makeText(
                        context, message, Toast.LENGTH_SHORT
                    ).show()
                }
            }) {
                ListPets(viewModel.animals, viewModel.isLoadingPets, goToPetInformation)
            }
        }
    }
}


@Composable
fun ListPets(animals: List<Animal> = emptyList(), isLoadingAnimals: Boolean, goToPetInformation: (id: Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        content = {
            if (!isLoadingAnimals && animals.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    NoPetFound()
                }
            } else {
                items(animals.size) { i ->
                    PetCard(animals[i]) {
                        goToPetInformation(animals[i].id)
                    }
                }
            }
        }
    )
}

@Composable
fun PetCard(animal: Animal, goToPetInformation: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp) // Altura mais compacta para lista vertical
            .padding(vertical = 4.dp)
            .clickable { goToPetInformation() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem dentro de um círculo
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                PetImage(pet = animal)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = animal.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                SexIcon(sex = animal.sex)
            }
        }
    }
}

@Composable
fun SexIcon(sex: String, modifier: Modifier = Modifier) {
    val (icon, color) = when (sex) {
        "Macho" -> Icons.Filled.Male to MaterialTheme.colorScheme.primary
        "Fêmea" -> Icons.Filled.Female to MaterialTheme.colorScheme.secondary
        else -> Icons.Filled.HelpOutline to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        imageVector = icon,
        contentDescription = sex,
        tint = color,
        modifier = modifier.size(20.dp)
    )
}



@Composable
fun PetSpecie(species: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .background(
                MaterialTheme.colorScheme.onPrimaryContainer,
                shape = androidx.compose.foundation.shape.CircleShape
            )
    ) {
        Text(
            text = if (species == "dog") "🐶" else "😺", // Emoji do texto
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
fun NoPetFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Pets Icon",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "Nenhum pet encontrado.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Cadastre um novo pet para começar!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}


@Composable
@Preview
fun MyPetsScreenPreview() {
    CarteiraPetTheme {
        ListPets(emptyList(), false, {})
    }
}
