package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import coil.ImageLoader
import coil.ImageLoaderFactory
import org.hyperskill.app.analytic.injection.PlatformAnalyticComponent
import org.hyperskill.app.android.core.extensions.NotificationChannelInitializer
import org.hyperskill.app.android.core.injection.AndroidAppComponent
import org.hyperskill.app.android.core.injection.AndroidAppComponentImpl
import org.hyperskill.app.android.profile_settings.view.mapper.asNightMode
import org.hyperskill.app.android.util.DebugToolsHelper
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.remote.UserAgentInfo
import ru.nobird.android.view.base.ui.extension.isMainProcess

class HyperskillApp : Application(), ImageLoaderFactory {
    companion object {
        lateinit var application: HyperskillApp

        fun getAppContext(): Context =
            application.applicationContext

        fun graph(): AndroidAppComponent =
            application.appGraph

        fun setIsLeaderboardTabEnabled(isEnabled: Boolean) {
            application.isLeaderboardTabEnabled = isEnabled
        }

        fun isLeaderboardTabEnabled(): Boolean =
            application.isLeaderboardTabEnabled
    }

    private lateinit var appGraph: AndroidAppComponent

    private var isLeaderboardTabEnabled: Boolean = false

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess) return

        setTheme(R.style.AppTheme)

        application = this

        DebugToolsHelper.initDebugTools(this)

        appGraph = AndroidAppComponentImpl(
            application = this,
            userAgentInfo = buildUserAgentInfo(),
            buildVariant = BuildVariant.getByValue(BuildConfig.BUILD_TYPE)!!
        )

        setNightMode(appGraph)
        initSentry(appGraph)
        initAnalyticEngines(appGraph.platformAnalyticComponent)
        initChannels()
    }

    override fun newImageLoader(): ImageLoader =
        graph().imageLoadingComponent.imageLoader

    private fun initSentry(appGraph: AppGraph) {
        appGraph.sentryComponent.sentryInteractor.setup()
    }

    private fun initAnalyticEngines(platformAnalyticComponent: PlatformAnalyticComponent) {
        platformAnalyticComponent.appsFlyerAnalyticEngine.startup()
        platformAnalyticComponent.amplitudeAnalyticEngine.startup(this)
    }

    private fun buildUserAgentInfo() =
        UserAgentInfo(
            BuildConfig.VERSION_NAME,
            "Android ${Build.VERSION.SDK_INT}",
            BuildConfig.VERSION_CODE.toString(),
            BuildConfig.APPLICATION_ID
        )

    private fun initChannels() {
        NotificationChannelInitializer.initNotificationChannels(this)
    }

    private fun setNightMode(appGraph: AppGraph) {
        val profileSettings = appGraph.buildProfileSettingsComponent().profileSettingsInteractor.getProfileSettings()
        AppCompatDelegate.setDefaultNightMode(
            profileSettings.theme.asNightMode()
        )
    }
}