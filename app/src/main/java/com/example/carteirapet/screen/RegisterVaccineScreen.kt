package com.example.carteirapet.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.QRCodeVaccine
import com.example.carteirapet.screen.components.ShareLinkButton
import com.example.carteirapet.viewModels.CreateVaccineRequestViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVaccineScreen(
    petId: Int? = null,
    goToVaccineCardScreen: () -> Unit,
    viewModel: CreateVaccineRequestViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    LaunchedEffect(petId) {
        if (petId != null) {
            viewModel.loadActiveVaccineRequestByAnimal(petId, onSuccessful = {}, onError = { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            })
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Vaccines,
                            contentDescription = "Nova Vacina",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Nova Vacina",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = goToVaccineCardScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (viewModel.isLoadingActiveVaccineRequest) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            } else {
                if (viewModel.vaccineRequest == null) {
                    CreateVaccineRequestContent(petId = petId, viewModel = viewModel)
                } else {
                    VaccineRequestCreatedContent(vaccineRequest = viewModel.vaccineRequest!!, petId = petId, onConclude = goToVaccineCardScreen)
                }
            }
        }
    }
}

@Composable
fun CreateVaccineRequestContent(petId: Int?, viewModel: CreateVaccineRequestViewModel){
    Column {
        Text(
            text = "Crie um novo registro de vacina para seu pet." +
                    " Esse registro só pode ser aceito, preenchido e assinado pelo seu médico veterinário."
        )
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Cada registro de vacina criado tem um tempo de expiração de 1 hora para que possa ser aceito pelo seu médico veterinário.")
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Após a criação, será gerado um QRcode e um link que pode ser escaneado ou enviado para seu médico veterinário.")
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Vamos começar? \uD83D\uDE0A")
        Spacer(modifier = Modifier.size(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary),
                onClick = {
                    if (petId != null) {
                        viewModel.createVaccineRequest(
                            petId = petId,
                            onSuccessful = {},
                            onError = {})
                    }
                }
            ) {
                Text(text = "Criar solicitação de vacina")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VaccineRequestCreatedContent(
    vaccineRequest: VaccineRequestResponse,
    petId: Int?,
    onConclude: () -> Unit
) {
    // Tentar parsear as datas com fallback
    val requestDateInstant = try {
        Instant.parse(vaccineRequest.requestDate)
    } catch (e: DateTimeParseException) {
        Instant.now() // Fallback para data atual
    }
    val expirationDateInstant = try {
        Instant.parse(vaccineRequest.expirationDate)
    } catch (e: DateTimeParseException) {
        Instant.now().plusSeconds(3600) // Fallback para 1 hora a partir de agora
    }

    // Converter para fuso horário local
    val requestDate = requestDateInstant.atZone(ZoneId.systemDefault())
    val expirationDate = expirationDateInstant.atZone(ZoneId.systemDefault())
    val totalDuration = expirationDate.toInstant().toEpochMilli() - requestDate.toInstant().toEpochMilli()

    // Estado para progresso e tempo restante
    var progress by remember { mutableFloatStateOf(1f) }
    var timeRemainingMillis by remember { mutableFloatStateOf(totalDuration.toFloat()) }

    // Atualizar progresso em tempo real
    LaunchedEffect(Unit) {
        while (true) {
            val currentTime = Instant.now().toEpochMilli()
            timeRemainingMillis = (expirationDate.toInstant().toEpochMilli() - currentTime).toFloat()
            progress = if (timeRemainingMillis > 0) timeRemainingMillis / totalDuration else 0f
            delay(1000L) // Atualiza a cada segundo
        }
    }

    // Animação suave para a barra de progresso
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )

    // Formatar datas para exibição
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val minutesRemaining = (timeRemainingMillis / 1000 / 60).toInt()
    val secondsRemaining = (timeRemainingMillis / 1000 % 60).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Solicitação de vacina criada com sucesso!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Peça ao seu médico veterinário para aceitar a solicitação escaneando o QR code ou acessando o link abaixo.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "QR Code",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight(800)
            )
            Spacer(modifier = Modifier.size(2.dp))
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QRCodeVaccine(inputText = "app://moo/createVaccineRequest/${vaccineRequest.id}")
            }
        }

        ShareLinkButton(url = "app://moo/createVaccineRequest/${vaccineRequest.id ?: 1}")

        // Informações de expiração e barra de progresso
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (timeRemainingMillis > 0) {
                    "Tempo para o link expirar: $minutesRemaining min $secondsRemaining s"
                } else {
                    "Solicitação expirada"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (progress > 0.3f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            if (timeRemainingMillis > 0) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = if (progress > 0.3f) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360, heightDp = 640, name = "Expirado")
@Composable
fun VaccineRequestCreatedContentPreviewExpired() {
    MaterialTheme {
        VaccineRequestCreatedContent(
            vaccineRequest = VaccineRequestResponse(
                id = 1,
                status = "CRIADO",
                animalName = "Rex",
                animalSpecies = "Cachorro",
                requestDate = "2025-04-28T15:01:20.249Z",
                expirationDate = "2025-04-28T15:01:20.249Z" // Já expirado
            ),
            petId = 1,
            onConclude = {}
        )
    }
}
