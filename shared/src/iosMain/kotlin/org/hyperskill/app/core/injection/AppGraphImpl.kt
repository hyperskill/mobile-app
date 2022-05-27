package org.hyperskill.app.core.injection

import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthComponentManual
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.main.injection.MainComponentManual
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepComponentManual

class AppGraphImpl(
    userAgentInfo: UserAgentInfo
) : iOSAppComponent {
    override val commonComponent: CommonComponent =
        CommonComponentImpl(userAgentInfo)

    override val networkComponentManual: NetworkComponentManual =
        NetworkComponentImpl(this)

    override val authComponentManual: AuthComponentManual =
        AuthComponentImpl(this)

    override val mainComponentManual: MainComponentManual =
        MainComponentImpl(this)

    override fun buildStepComponent(): StepComponentManual =
        StepComponentImpl(this)
}