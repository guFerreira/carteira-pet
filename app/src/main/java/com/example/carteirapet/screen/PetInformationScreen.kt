package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.R
import com.example.carteirapet.repositories.Animal
import com.example.carteirapet.repositories.Vaccine
import com.example.carteirapet.viewModels.PetInformationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetInformation(
    petId: Int? = null,
    goToHomeScreen: () -> Unit,
    goRegisterVaccineScreen: () -> Unit,
    viewModel: PetInformationViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    LaunchedEffect(petId) {
        viewModel.isLoading = true
        if (petId != null) {
            viewModel.loadPetInformation(petId, onError = { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            })
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
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(120.dp),
                onClick = goRegisterVaccineScreen,
                icon = { Icon(Icons.Filled.Add, "Extended floating action button.") },
                text = { Text(text = "Registrar vacina") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (viewModel.isLoading){
                    CircularProgressIndicator()
                } else {
                    PetInformations(viewModel.pet)
                    Vaccines(viewModel.vaccines)
                }
            }
        }
    }
}

@Composable
fun PetInformations(pet: Animal?) {
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (pet == null){
            Text(text = "Carregando informações do pet...")
        } else {
//            Image(
//                painter = painterResource(id = R.drawable.bolinha),
//                contentDescription = "Imagem do pet",
//                Modifier
//                    .border(3.dp, MaterialTheme.colorScheme.onSecondaryContainer, CircleShape)
//                    .size(120.dp)
//            )
            // TODO transformar isso em um componente
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.onSecondaryContainer, shape = CircleShape) // Adiciona a cor de fundo
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        CircleShape
                    )
                    .size(120.dp),
                contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box
            ) {
                Text(
                    text = if (pet.species == "dog") "🐶" else "😺",
                    fontSize = 80.sp // Ajuste o tamanho do emoji conforme necessário
                )
            }
            Column {
                Text(
                    text = pet.name, style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                    )
                )
                Text(
                    text = "Raça: ${pet.breeds.first().name}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )
                Text(
                    text = "Data de Nascimento: ${pet.birthDate}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )
                Text(
                    text = "Sexo: ${pet.sex}", style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                    )
                )
                Text(
                    text = "Castrado: ${if (pet.neutered == true) "Sim" else "Não"}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,fontSize = 14.sp,
                    )
                )
            }
        }
    }
}

@Composable
fun Vaccines(vaccines: List<Vaccine>) {
    if (vaccines.isEmpty()){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // Centraliza o conteúdo dentro do Box
        ) {
            Text(text = "Nenhuma vacina registrada \uD83D\uDC89")
        }
    } else{
        LazyColumn() {
            items(vaccines.size) { item ->
                VaccineItem(vaccines[item], modifier = Modifier.padding(4.dp))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineItem(vaccine: Vaccine, modifier: Modifier) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Card(
        onClick = { showBottomSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,// Defina a cor desejada aqui
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer // Defina a cor do texto desejada aqui
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = vaccine.applicationDate,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = vaccine.name,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Status da assinatura: ${vaccine.status}",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.height(1.dp))

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = vaccine.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Data da aplicação: ${vaccine.applicationDate}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Lote: ${vaccine.batchCode}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Fabricante: ${vaccine.manufacturer}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "Médico veterinário: ${vaccine.veterinaryDoctorName}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = "CRMV: ${vaccine.crmv}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "Status do documento: ${vaccine.status}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = "Documento assinado digitalmente",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                        )
                        Text(
                            text = vaccine.signedPdf,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}

@Composable
@Preview
fun PetInformationPreview() {
    PetInformation(0, {}, {})
}