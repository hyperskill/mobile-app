package org.hyperskill.app.core.injection

import android.content.Context
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent

interface AndroidAppComponent : AppGraph {
    val context: Context
    val platformMainComponent: PlatformMainComponent

    fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent
    fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent
    fun buildPlatformAuthCredentialsComponent(authCredentialsComponent: AuthCredentialsComponent): PlatformAuthCredentialsComponent
    fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent
    fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent
}