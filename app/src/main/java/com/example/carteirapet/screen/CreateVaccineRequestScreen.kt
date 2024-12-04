package com.example.carteirapet.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.carteirapet.R
import com.example.carteirapet.viewModels.CreateVaccineRequestViewModel
import com.example.carteirapet.viewModels.EditUserProfileViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateVaccineRequestScreen(petId: Int? = null, goToHomeScreen: () -> Unit, goToVaccineRequestFormScreen: (id: Int) -> Unit, viewModel: CreateVaccineRequestViewModel = koinViewModel()){
    val context = LocalContext.current
    Scaffold(
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding).padding(16.dp).fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.gatinho_carteira),
                contentDescription = "Pet segurando carteira",
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
            Text("Você escaneou o QR code ou clicou no link com sucesso.", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center,)
            Text("Agora, ao clicar em \"Criar\", você poderá iniciar uma nova solicitação de vacinação.", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (petId == null) return@Button
                viewModel.createVaccineRequest(petId, onSuccessful = goToVaccineRequestFormScreen , onError = { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                } )

            }, modifier = Modifier.width(200.dp)) {
                if(viewModel.isLoading){
                    CircularProgressIndicator()
                } else {
                    Text(text = "Criar")
                }
            }
        }
    }
}


@Preview
@Composable
fun CreateVaccineRequestScreenPreview() {
    CreateVaccineRequestScreen(1, {}, {})
}
