package org.hyperskill.app.android.core.injection

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import org.hyperskill.app.BuildConfig

@Module
object SharedPreferencesModule {
    @Provides
    @JvmStatic
    fun provideAndroidSettings(sharedPreferences: SharedPreferences): Settings =
        AndroidSettings(sharedPreferences)

    @Provides
    @JvmStatic
    fun provideSharedPreferences(context: Context): SharedPreferences =
        if (BuildConfig.DEBUG) {
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        } else {
            EncryptedSharedPreferences.create(
                context,
                "app_preferences",
                getMasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

    private fun getMasterKey(context: Context): MasterKey =
        MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}