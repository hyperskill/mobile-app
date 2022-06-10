package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import android.os.Build
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.fragment.FragmentLifecycleIntegration
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.injection.AndroidAppComponent
import org.hyperskill.app.core.injection.AndroidAppComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import ru.nobird.android.view.base.ui.extension.isMainProcess

class HyperskillApp : Application() {
    companion object {
        lateinit var application: HyperskillApp

        fun getAppContext(): Context =
            application.applicationContext

        fun graph(): AndroidAppComponent =
            application.appGraph
    }

    private lateinit var appGraph: AndroidAppComponent

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess) return

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            SoLoader.init(this, false)

            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }

        setTheme(R.style.AppTheme)

        application = this

        appGraph = AndroidAppComponentImpl(this, buildUserAgentInfo())
        initSentry()
    }

    private fun initSentry() {
        SentryAndroid.init(application) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.environment = "${BuildKonfig.FLAVOR}-${BuildConfig.BUILD_TYPE}"
            options.release = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            options.isEnableAutoSessionTracking = true
            options.isAnrEnabled = true
            options.addIntegration(
                FragmentLifecycleIntegration(
                    application,
                    enableFragmentLifecycleBreadcrumbs = true,
                    enableAutoFragmentLifecycleTracing = true
                )
            )

            if (BuildConfig.DEBUG) {
                options.setDebug(true)
                options.setDiagnosticLevel(SentryLevel.WARNING)
                options.tracesSampleRate = 1.0
            } else {
                options.setDebug(false)
                options.tracesSampleRate = 0.3
            }
        }
    }

    private fun buildUserAgentInfo() =
        UserAgentInfo(
            BuildConfig.VERSION_NAME,
            "Android ${Build.VERSION.SDK_INT}",
            BuildConfig.VERSION_CODE.toString(),
            BuildConfig.APPLICATION_ID
        )
}