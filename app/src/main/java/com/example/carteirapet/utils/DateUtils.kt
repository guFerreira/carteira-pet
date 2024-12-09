package com.example.carteirapet.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {

    fun formatDateStringToShow(dateString:String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun formatDateStringToRegister(dateString:String, outputFormat:String = "yyyy/MM/dd"): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}