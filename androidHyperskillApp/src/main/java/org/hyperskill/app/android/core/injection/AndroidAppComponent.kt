package org.hyperskill.app.android.core.injection

import android.content.Context
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthCredentialsComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialComponent
import org.hyperskill.app.auth.injection.PlatformAuthSocialWebViewComponent
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.home.injection.HomeComponent
import org.hyperskill.app.home.injection.PlatformHomeComponent
import org.hyperskill.app.main.injection.PlatformMainComponent
import org.hyperskill.app.profile.injection.PlatformProfileComponent
import org.hyperskill.app.profile.injection.ProfileComponent
import org.hyperskill.app.step.injection.PlatformStepComponent
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.PlatformStepQuizComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent
import org.hyperskill.app.track.injection.PlatformTrackComponent
import org.hyperskill.app.track.injection.TrackComponent

interface AndroidAppComponent : AppGraph {
    val context: Context
    val platformMainComponent: PlatformMainComponent
    val platformNotificationComponent: PlatformNotificationComponent

    fun buildPlatformAuthSocialWebViewComponent(): PlatformAuthSocialWebViewComponent
    fun buildPlatformAuthSocialComponent(authSocialComponent: AuthSocialComponent): PlatformAuthSocialComponent
    fun buildPlatformAuthCredentialsComponent(authCredentialsComponent: AuthCredentialsComponent): PlatformAuthCredentialsComponent
    fun buildPlatformStepComponent(stepComponent: StepComponent): PlatformStepComponent
    fun buildPlatformStepQuizComponent(stepQuizComponent: StepQuizComponent): PlatformStepQuizComponent
    fun buildPlatformLatexComponent(): PlatformLatexComponent
    fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent
    fun buildPlatformTrackComponent(trackComponent: TrackComponent): PlatformTrackComponent
    fun buildPlatformProfileComponent(profileComponent: ProfileComponent): PlatformProfileComponent
    fun buildPlatformHomeComponent(homeComponent: HomeComponent): PlatformHomeComponent
}