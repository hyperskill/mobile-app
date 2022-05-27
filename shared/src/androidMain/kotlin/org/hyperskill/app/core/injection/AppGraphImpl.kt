package org.hyperskill.app.core.injection

import android.app.Application
import android.content.Context
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthComponentManual
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.main.injection.MainComponentManual
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepComponentManual

class AppGraphImpl(
    private val application: Application,
    userAgentInfo: UserAgentInfo
) : AndroidAppComponent {
    override val context: Context
        get() = application

    override val commonComponent: CommonComponent =
        CommonComponentImpl(application, userAgentInfo)

    override val mainComponentManual: MainComponentManual =
        MainComponentImpl(this)

    override val networkComponentManual: NetworkComponentManual =
        NetworkComponentImpl(this)

    override val authComponentManual: AuthComponentManual =
        AuthComponentImpl(this)

    override fun buildStepComponent(): StepComponentManual =
        StepComponentImpl(this)
}