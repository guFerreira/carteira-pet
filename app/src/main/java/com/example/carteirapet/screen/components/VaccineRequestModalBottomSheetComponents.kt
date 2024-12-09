package com.example.carteirapet.screen.components

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ButtonDownloadPdf(pdfUrl: String) {
    val context = LocalContext.current

    Button(onClick = {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle("Baixando PDF")
            .setDescription("Fazendo download do arquivo PDF")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "meu_arquivo.pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }) {
        Text("Baixar PDF")
    }
}

@Composable
fun ButtonOpenPdfOnBrowser(pdfUrl: String) {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
        context.startActivity(intent)
    }) {
        Text("Abrir PDF")
    }
}

@Composable
fun ButtonOpenLinkForDigitalSignatureOnBrowser(url: String) {
    val context = LocalContext.current

    Button(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }) {
        Text("Assinar digitalmente")
    }
}

@Composable
fun VaccineActions(
    status: String?,
    pdfDocumentUrl: String?,
    signatureUrl: String?,
    isVeterinary: Boolean = false,
    goToUpdateVaccineRequestScreen: () -> Unit = {}
) {
    if (status == "assinado") {
        Divider(
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pdfDocumentUrl?.let { ButtonDownloadPdf(pdfUrl = it) }
            pdfDocumentUrl?.let { ButtonOpenPdfOnBrowser(pdfUrl = it) }
        }
    } else {
        if (isVeterinary) {
            if (signatureUrl != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { goToUpdateVaccineRequestScreen() }) {
                        Text(text = "Editar registro de vacina")
                    }
                    ButtonOpenLinkForDigitalSignatureOnBrowser(url = signatureUrl ?: "")
                }
            } else {
                Row {
                    Button(onClick = { goToUpdateVaccineRequestScreen() }) {
                        Text(text = "Concluir registro de vacina")
                    }
                }
            }
        } else {
            Text(text = "A vacina ainda não foi assinada digitalmente.")
        }

    }
}