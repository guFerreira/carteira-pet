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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.carteirapet.screen.components.SexIcon
import com.example.carteirapet.screen.components.TagChip
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                // Removendo 'colors' para usar os valores padrão do tema M3.
                // As cores do seu tema personalizado serão aplicadas automaticamente.
                title = {
                    Logo() // O logo já está centralizado com CenterAlignedTopAppBar
                },
                actions = {
                    IconButton(
                        onClick = { goToEditUserProfileScreen() },
                        // Modificador para aumentar a área de clique, conforme M3
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Editar perfil do usuário", // Descrição mais específica
                            // Removendo 'tint' para usar a cor padrão do tema M3 (onSurfaceVariant ou onSurface)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.logout(goToLoginScreen, onError = { message ->
                                Toast.makeText(
                                    context, message, Toast.LENGTH_SHORT
                                ).show()
                            })
                        },
                        // Modificador para aumentar a área de clique
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "Sair da conta", // Descrição mais clara
                            // Removendo 'tint' para usar a cor padrão do tema M3
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            // Usando o ExtendedFloatingActionButton do M3
            ExtendedFloatingActionButton(
                // Removendo 'containerColor' e 'contentColor' para usar os valores padrão do tema M3 para FAB
                // O M3 usa 'primaryContainer' e 'onPrimaryContainer' por padrão para FABs estendidos.
                onClick = goToRegisterPetScreen,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Adicionar novo pet"
                    )
                },
                text = { Text(text = "Registrar Novo Pet") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp) // Mantendo este padding para o conteúdo da coluna
                .fillMaxSize(), // Usar fillMaxSize para a coluna para que o LazyVerticalGrid possa rolar
        ) {
            CardUser(viewModel.name, false)

            // Mover o PullToRefreshBox para dentro de um Modifier.weight(1f)
            // Isso permite que o PullToRefreshBox (e ListPets dentro dele) preencha o espaço restante
            PullToRefreshBox(isRefreshing = viewModel.isLoadingPets, onRefresh = {
                viewModel.loadAnimals { message ->
                    Toast.makeText(
                        context, message, Toast.LENGTH_SHORT
                    ).show()
                }
            }, modifier = Modifier.weight(1f)) { // Adicionar modifier para PullToRefreshBox
                ListPets(viewModel.animals, viewModel.isLoadingPets, goToPetInformation)
            }
        }
    }
}

@Composable
fun ListPets(animals: List<Animal> = emptyList(), isLoadingAnimals: Boolean, goToPetInformation: (id: Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1), // Uma coluna por design
        verticalArrangement = Arrangement.spacedBy(12.dp), // Espaçamento entre os itens na vertical
        horizontalArrangement = Arrangement.spacedBy(12.dp), // Espaçamento entre os itens na horizontal (não aplicável a 1 coluna, mas bom para consistência)
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp), // Padding superior para separar da CardUser
        content = {
            if (isLoadingAnimals && animals.isEmpty()) {
                // Exibe um indicador de carregamento se estiver carregando e não houver animais
                item(span = { GridItemSpan(1) }) { // Use GridItemSpan(1) para uma única coluna
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(0.6f)) // Indicador de progresso no meio da tela
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Carregando seus pets...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (!isLoadingAnimals && animals.isEmpty()) {
                item(span = { GridItemSpan(1) }) { // Use GridItemSpan(1) para uma única coluna
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
    ElevatedCard( // Usar ElevatedCard do M3
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh, // Cor de fundo para Cards (mais proeminente que surfaceContainer)
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp) // Altura mais compacta para lista vertical
            // Remover padding vertical aqui para que o espaçamento seja controlado por verticalArrangement no LazyVerticalGrid
            .clickable { goToPetInformation() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Aumentar o padding interno do Card
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem dentro de um círculo
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape) // Usar clip para garantir a forma circular
                    .background(MaterialTheme.colorScheme.secondaryContainer), // Cor do tema
                contentAlignment = Alignment.Center
            ) {
                PetImage(pet = animal, modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.width(16.dp)) // Espaçamento consistente

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f) // Fazer a coluna de texto preencher o espaço restante
            ) {
                Text(
                    text = animal.name,
                    style = MaterialTheme.typography.titleMedium, // Título do pet
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp)) // Pequeno espaçamento entre nome e sexo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SexIcon(sex = animal.sex)
                    Spacer(modifier = Modifier.width(8.dp)) // Espaçamento entre ícone de sexo e espécie
                    PetSpecie(species = animal.species) // Adicionando a espécie
                }
            }
        }
    }
}



@Composable
fun PetSpecie(species: String) {
    // TagChip para Espécie
    val speciesIcon = Icons.Default.Pets
    val speciesText = if (species == "DOG") "Cachorro" else "Gato"
    val containerColor = MaterialTheme.colorScheme.tertiaryContainer
    val contentColor = MaterialTheme.colorScheme.onTertiaryContainer

    Surface(
        shape = CircleShape, // Formato de "pílula"
        color = containerColor,
        tonalElevation = 2.dp // Uma leve elevação para destacar
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = speciesIcon,
                contentDescription = null, // O texto já descreve
                tint = contentColor,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = speciesText,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        }
    }
}


@Composable
fun NoPetFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp) // Mais espaçamento vertical para centralizar
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Nenhum pet encontrado", // Descrição para acessibilidade
            modifier = Modifier.size(48.dp), // Ícone maior
            tint = MaterialTheme.colorScheme.outline // Cor discreta para ícone de aviso
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espaçamento
        Text(
            text = "Nenhum pet encontrado.",
            style = MaterialTheme.typography.titleMedium, // Título médio para a mensagem
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Cor para texto secundário
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Cadastre um novo pet para começar!",
            style = MaterialTheme.typography.bodyLarge, // Texto de corpo para instrução
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
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
