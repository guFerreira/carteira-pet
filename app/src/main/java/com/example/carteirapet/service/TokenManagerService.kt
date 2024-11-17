package com.example.carteirapet.service
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenManagerService(context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "jwt_shared_prefs",  // Nome do SharedPreferences
        masterKeyAlias,      // Chave de criptografia
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Função para salvar tokens
    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
        }.commit()  // Alterado para commit() que é síncrono
    }


    // Função para obter o access token
    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)

    // Função para obter o refresh token
    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)

    // Função para limpar os tokens (em caso de logout, por exemplo)
    fun clearTokens() {
        sharedPreferences.edit().clear().commit()
    }
}