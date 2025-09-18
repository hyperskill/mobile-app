package org.hyperskill.app.android.clarity

import android.content.Context
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
        private const val IS_DEBUG_KEY = "IS_DEBUG"
        private val clarityConfig = ClarityConfig(
            projectId = BuildConfig.CLARITY_PROJECT_ID,
            logLevel = if (BuildConfig.DEBUG) LogLevel.Debug else LogLevel.None,
        )
    }

    private var isFirstSession: Boolean = false

    fun init(context: Context, savedStateRegistry: SavedStateRegistry) {
        registerSavedStateProvider(savedStateRegistry)
        isFirstSession = isFirstSession(savedStateRegistry, settings)
        setNotFirstSession(settings)
        if (isFirstSession) {
            Clarity.initialize(context, clarityConfig)
            Clarity.setCustomTag(IS_DEBUG_KEY, BuildConfig.DEBUG.toString())
        }
    }

    fun setUserId(userId: Long) {
        if (isFirstSession) {
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