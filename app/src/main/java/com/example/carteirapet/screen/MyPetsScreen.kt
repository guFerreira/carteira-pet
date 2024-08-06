package com.example.carteirapet.screen

import android.content.Intent
import android.content.res.Resources.Theme
import android.graphics.drawable.shapes.Shape
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.R
import com.example.carteirapet.ui.theme.CarteiraPetTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPetsScreen() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* Handle the result if needed */ }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

     val pets = remember { mutableStateListOf("Bolinha", "Peludinho", "Rex") }
//    val pets = remember { mutableStateListOf<String>() }

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
                        Icon(
                            imageVector = Icons.Filled.Pets,
                            contentDescription = "Pets Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Moo",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                },
//                navigationIcon = {
//                    IconButton(onClick = { /* do something */ }) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(100.dp),
                onClick = { },
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Registrar Novo Pet") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Olá, Tutor-teste!",
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Light,
                    text = "Selecione um de seus pets para visualizar as suas vacinas",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            if (pets.isEmpty()) {
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

            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    content = {

                        items(pets.size) { i ->

                            ElevatedCard(
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                modifier = Modifier
                                    .size(width = 80.dp, height = 92.dp),
                                onClick = {
                                    val intent =
                                        Intent(context, PetInformationScreen::class.java).apply {
                                            // Pass data as an extra with the intent
                                            putExtra("MESSAGE", "Hello from Compose!")
                                        }
                                    // Start the activity using the launcher
                                    launcher.launch(intent)
                                }
                            ) {
                                Column (modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.Start,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()

                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.bolinha),
                                            contentDescription = "Imagem do pet",
                                            modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                                    CircleShape
                                                )
                                                .size(60.dp)
                                        )
                                        Text(
                                            text = pets[i],
                                            modifier = Modifier.padding(start = 4.dp)
                                        )

                                    }

                                    Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(16.dp) // Define o tamanho do círculo
                                                .background(MaterialTheme.colorScheme.onPrimaryContainer, shape = androidx.compose.foundation.shape.CircleShape)
                                        ) {
                                            Text(
                                                text = "\uD83D\uDC36", // Emoji do texto
                                                style = TextStyle(
                                                    color = Color.White, // Cor do texto
                                                    textAlign = TextAlign.Center,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }



                            }
                        }
                    })
            }
        }
    }
}

@Composable
@Preview
fun MyPetsScreenPreview() {
    CarteiraPetTheme {
        MyPetsScreen()
    }
}


@Composable
fun CardPet(showPetInfos: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .padding(8.dp)
        .clickable {
            showPetInfos()
        }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bolinha),
                contentDescription = "Imagem do pet",
                Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer, CircleShape)
                    .size(120.dp)
            )
            Text(text = "Bolinha")
        }
    }
}
