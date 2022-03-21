package org.hyperskill.app.android

import android.app.Application
import android.content.Context
import org.hyperskill.app.android.core.injection.AppCoreComponent
import org.hyperskill.app.android.core.injection.DaggerAppCoreComponent
import ru.nobird.android.view.base.ui.extension.isMainProcess

class HyperskillApp : Application() {
    companion object {
        lateinit var application: HyperskillApp

        fun component(): AppCoreComponent =
            application.component

        fun getAppContext(): Context =
            application.applicationContext
    }

    private lateinit var component: AppCoreComponent

    override fun onCreate() {
        super.onCreate()
        if (!isMainProcess) return

        application = this

        component = DaggerAppCoreComponent.builder()
            .context(application)
            .build()

        component.inject(this)
    }
}