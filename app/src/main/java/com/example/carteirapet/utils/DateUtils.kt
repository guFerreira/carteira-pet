package com.example.carteirapet.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {

    fun formatDateStringToShow(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun formatDateStringToRegister(
        dateString: String,
        outputFormat: String = "yyyy/MM/dd"
    ): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }


    fun getTimeRemainingText(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // entrada em UTC

        val expirationDate = inputFormat.parse(dateString) ?: return "Data inválida"
        val now = Date() // hora atual no timezone do dispositivo

        val diffMillis = expirationDate.time - now.time

        if (diffMillis <= 0) {
            return "Expirado"
        }

        val seconds = (diffMillis / 1000) % 60
        val minutes = (diffMillis / (1000 * 60)) % 60
        val hours = (diffMillis / (1000 * 60 * 60)) % 24
        val days = diffMillis / (1000 * 60 * 60 * 24)

        return buildString {
            if (days > 0) append("$days dia(s) ")
            if (hours > 0 || days > 0) append("$hours hora(s) ")
            if (minutes > 0 || hours > 0 || days > 0) append("$minutes min ")
            append("$seconds seg")
        }.trim()
    }
}