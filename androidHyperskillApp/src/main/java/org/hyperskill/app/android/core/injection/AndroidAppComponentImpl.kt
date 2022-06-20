package org.hyperskill.app.android.core.injection

import android.app.Application
import android.content.Context
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponentImpl
import org.hyperskill.app.auth.injection.AuthComponentImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.AuthSocialComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponentImpl
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponentImpl
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.core.injection.CommonComponentImpl
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.main.injection.MainComponentImpl
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.main.injection.PlatformMainComponentImpl
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.network.injection.NetworkComponentImpl
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.PlatformStepComponentImpl
import org.hyperskill.app.step.injection.StepComponentImpl
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponentImpl
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponentImpl

class AndroidAppComponentImpl(
    private val application: Application,
    userAgentInfo: UserAgentInfo
) : AndroidAppComponent {
    override val context: Context
        get() = application

    override val commonComponent: CommonComponent =
        CommonComponentImpl(application, userAgentInfo)

    override val mainComponent: MainComponent =
        MainComponentImpl(this)

    override val platformMainComponent: PlatformMainComponent =
        PlatformMainComponentImpl(mainComponent)

    override val networkComponent: NetworkComponent =
        NetworkComponentImpl(this)

    override val authComponent: AuthComponent =
        AuthComponentImpl(this)

    override fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent =
        PlatformAuthSocialWebViewComponentImpl()

    /**
     * Auth social component
     */
    override fun buildAuthSocialComponent(): AuthSocialComponent =
        AuthSocialComponentImpl(commonComponent, authComponent)

    override fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent =
        PlatformAuthSocialComponentImpl(authSocialComponent)

    /**
     * Auth credentials component
     */
    override fun buildAuthCredentialsComponent(): AuthCredentialsComponent =
        AuthCredentialsComponentImpl(commonComponent, authComponent)

    override fun buildPlatformAuthCredentialsComponent(authCredentialsComponent: AuthCredentialsComponent): PlatformAuthCredentialsComponent =
        PlatformAuthCredentialsComponentImpl(authCredentialsComponent)

    /**
     * Step component
     */
    override fun buildStepComponent(): StepComponent =
        StepComponentImpl(this)

    override fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent =
        PlatformStepComponentImpl(stepComponent)

    /**
     * Step quiz component
     */
    override fun buildStepQuizComponent(): StepQuizComponent =
        StepQuizComponentImpl(this)

    override fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent =
        PlatformStepQuizComponentImpl(stepQuizComponent)

    /**
     * Latex component
     */
    override fun buildPlatformLatexComponent(): PlatformLatexComponent =
        PlatformLatexComponentImpl(application)
}