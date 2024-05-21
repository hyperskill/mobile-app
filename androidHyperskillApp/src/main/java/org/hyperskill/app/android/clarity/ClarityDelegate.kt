package org.hyperskill.app.android.clarity

import android.content.Context
import android.util.Log
import androidx.core.os.bundleOf
import androidx.savedstate.SavedStateRegistry
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.microsoft.clarity.models.LogLevel
import com.russhwolf.settings.Settings
import org.hyperskill.app.android.BuildConfig

class ClarityDelegate(
    private val settings: Settings
) {

    companion object {
        private const val SAVED_STATE_PROVIDER_KEY = "CLARITY_MANAGER"
        private const val IS_FIRST_SESSION_KEY = "IS_ANDROID_FIRST_SESSION"
        private val clarityConfig = ClarityConfig(
            projectId = "",
            logLevel = if (BuildConfig.DEBUG) LogLevel.Debug else LogLevel.None,
            allowMeteredNetworkUsage = false,
            allowedDomains = emptyList(),
            disableOnLowEndDevices = true
        )
    }

    private var isFirstSession: Boolean = false

    fun init(context: Context, savedStateRegistry: SavedStateRegistry) {
        registerSavedStateProvider(savedStateRegistry)
        isFirstSession = isFirstSession(savedStateRegistry, settings)
        setNotFirstSession(settings)
        Log.d("ClarityDelegate", "init: isFirstSession=$isFirstSession")
        if (isFirstSession) {
            Log.d("ClarityDelegate","Initialize Clarity")
            Clarity.initialize(context, clarityConfig)
        }
    }

    fun setUserId(userId: Long) {
        if (isFirstSession) {
            Log.d("ClarityDelegate","Set user id")
            Clarity.setCustomUserId(userId.toString())
        }
    }

    private fun isFirstSession(savedStateRegistry: SavedStateRegistry, settings: Settings): Boolean =
        getIsFirstSession(savedStateRegistry) ?: getIsFirstSession(settings)

    private fun getIsFirstSession(savedStateRegistry: SavedStateRegistry): Boolean? =
        savedStateRegistry
            .consumeRestoredStateForKey(SAVED_STATE_PROVIDER_KEY)
            ?.getBoolean(IS_FIRST_SESSION_KEY)

    private fun getIsFirstSession(settings: Settings): Boolean =
        settings.getBoolean(IS_FIRST_SESSION_KEY, defaultValue = true)

    private fun setNotFirstSession(settings: Settings) {
        settings.putBoolean(IS_FIRST_SESSION_KEY, false)
    }

    private fun registerSavedStateProvider(savedStateRegistry: SavedStateRegistry) {
        savedStateRegistry.registerSavedStateProvider(SAVED_STATE_PROVIDER_KEY) {
            bundleOf(IS_FIRST_SESSION_KEY to isFirstSession)
        }
    }
}