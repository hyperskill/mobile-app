package org.hyperskill.app.core.injection

import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl

class AppGraphImpl(
    userAgentInfo: UserAgentInfo
) : iOSAppComponent {
    override val commonComponent: CommonComponent =
        CommonComponentImpl(userAgentInfo)

    override val networkComponent: NetworkComponent =
        NetworkComponentImpl(this)

    override val authComponent: AuthComponent =
        AuthComponentImpl(this)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(commonComponent, authComponent)

    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(commonComponent, authComponent)

    override fun buildStepComponent(): StepComponent =
        StepComponentImpl(this)

    override fun buildStepQuizComponent(): StepQuizComponent =
        StepQuizComponentImpl(this)
}