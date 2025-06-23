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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carteirapet.repositories.VaccineRequestResponse
import com.example.carteirapet.screen.components.Logo
import com.example.carteirapet.screen.components.QRCodeVaccine
import com.example.carteirapet.screen.components.ShareLinkButton
import com.example.carteirapet.viewModels.CreateVaccineRequestViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
                        Logo()
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
                .padding(16.dp)
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
                    VaccineRequestCreatedContent(vaccineRequest = viewModel.vaccineRequest!!, onConclude = goToVaccineCardScreen)
                }
            }
        }
    }
}
@Composable
fun CreateVaccineRequestContent(
    modifier: Modifier = Modifier, // Este modifier vem do Scaffold e já contém o padding do TopAppBar
    petId: Int?,
    viewModel: CreateVaccineRequestViewModel
) {
    // A MÁGICA ACONTECE AQUI:
    // O Column precisa preencher todo o espaço que o Scaffold deu a ele.
    Column(
        modifier = modifier // 1. Aplica o padding do Scaffold
            .fillMaxSize()    // 2. MANDA A COLUNA OCUPAR TODO O ESPAÇO RESTANTE
            .padding(16.dp),  // 3. Adiciona um padding interno para o conteúdo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Text, Spacer, Card, etc. continuam iguais)

        Text(
            text = "Gerar um código de autorização para o veterinário aplicar uma vacina.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

// Card para agrupar as informações importantes, dando destaque e organização.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                // ListItem é ideal para exibir um ícone + texto, melhorando a leitura.
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    headlineContent = { Text("Aprovação Veterinária") },
                    supportingContent = { Text("O registro só pode ser preenchido e assinado pelo seu veterinário.") }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    headlineContent = { Text("Expira em 1 hora") },
                    supportingContent = { Text("O código gerado deve ser usado pelo veterinário dentro de uma hora.") }
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    headlineContent = { Text("Gera QR Code e Link") },
                    supportingContent = { Text("Compartilhe o código gerado para que o veterinário acesse a solicitação.") }
                )
            }
        }

        // AGORA ISTO VAI FUNCIONAR:
        // Como o Column tem altura máxima, este Spacer tem espaço para "empurrar" o botão.
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (petId != null) {
                    viewModel.createVaccineRequest(
                        petId = petId,
                        onSuccessful = {},
                        onError = {})
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Ajustei para 48dp, um ótimo valor de toque
        ) {
            Icon(
                Icons.Default.QrCode2,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "Gerar Código de Solicitação")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VaccineRequestCreatedContent(
    modifier: Modifier = Modifier,
    vaccineRequest: VaccineRequestResponse,
    onConclude: () -> Unit
) {
    // --- LÓGICA DO CRONÔMETRO (Mantida, pois já é robusta) ---

    // Fallback seguro para as datas
    val expirationDateInstant = remember(vaccineRequest.expirationDate) {
        try {
            Instant.parse(vaccineRequest.expirationDate)
        } catch (e: DateTimeParseException) {
            Instant.now().plusSeconds(3600)
        }
    }
    val totalDuration = remember(vaccineRequest.requestDate, expirationDateInstant) {
        val requestDateInstant = try {
            Instant.parse(vaccineRequest.requestDate)
        } catch (e: DateTimeParseException) {
            Instant.now()
        }
        (expirationDateInstant.toEpochMilli() - requestDateInstant.toEpochMilli()).coerceAtLeast(1)
    }

    var timeRemainingMillis by remember { mutableFloatStateOf(totalDuration.toFloat()) }

    // Usar o ID da requisição como chave garante que o efeito reinicie se a requisição mudar.
    LaunchedEffect(key1 = vaccineRequest.id) {
        while (coroutineContext.isActive && timeRemainingMillis > 0) {
            val now = Instant.now().toEpochMilli()
            timeRemainingMillis = (expirationDateInstant.toEpochMilli() - now).toFloat()
            delay(1000L)
        }
        timeRemainingMillis = 0f // Garante que o tempo zere ao final
    }

    val progress = (timeRemainingMillis / totalDuration).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressAnimation"
    )

    // Formatação do tempo para exibição
    val minutesRemaining = (timeRemainingMillis / 1000 / 60).toInt()
    val secondsRemaining = (timeRemainingMillis / 1000 % 60).toInt()
    val isExpired = timeRemainingMillis <= 0

    // Condição de cor mais legível (ex: muda quando faltam menos de 5 minutos)
    val isUrgent = timeRemainingMillis < 5 * 60 * 1000 && !isExpired
    val progressColor = when {
        isExpired -> MaterialTheme.colorScheme.error
        isUrgent -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    // --- LAYOUT COM MATERIAL 3 ---

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = "Sucesso",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solicitação criada!",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Apresente o QR Code ou compartilhe o link com o veterinário para continuar.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- CARD AJUSTADO ---
        // 1. Não ocupa mais a largura toda (sem fillMaxWidth).
        // 2. Tem uma cor de container explícita para contraste.
        Card(
            shape = MaterialTheme.shapes.large, // Cantos mais arredondados
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp), // Padding generoso dentro do card
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Container do QR Code para dar um destaque visual
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium) // Cantos arredondados para o QR
                        .background(MaterialTheme.colorScheme.onPrimary) // Fundo branco para o QR Code
                        .padding(12.dp) // Um preenchimento para o QR Code não colar nas bordas
                ) {
                    QRCodeVaccine(inputText = "app://moo/createVaccineRequest/${vaccineRequest.id}")
                }

                Spacer(modifier = Modifier.height(20.dp))

                ShareLinkButton(url = "app://moo/createVaccineRequest/${vaccineRequest.id}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Seção de expiração
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (isExpired) "Solicitação expirada" else "Expira em: $minutesRemaining min $secondsRemaining s",
                style = MaterialTheme.typography.titleSmall,
                color = progressColor
            )
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Um pouco menor que a tela toda para elegância
                    .height(8.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Botão de conclusão com estilo secundário
        OutlinedButton(
            onClick = onConclude,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Concluir")
        }
    }
}
