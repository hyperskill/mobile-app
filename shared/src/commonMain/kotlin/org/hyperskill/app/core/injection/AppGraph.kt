package org.hyperskill.app.core.injection

import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.auth.injection.AuthCredentialsComponent
import org.hyperskill.app.auth.injection.AuthSocialComponent
import org.hyperskill.app.main.injection.MainComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.step.injection.StepComponent
import org.hyperskill.app.step_quiz.injection.StepQuizComponent

interface AppGraph {
    val commonComponent: CommonComponent
    val networkComponent: NetworkComponent
    val authComponent: AuthComponent
    val mainComponent: MainComponent

    fun buildAuthSocialComponent(): AuthSocialComponent
    fun buildAuthCredentialsComponent(): AuthCredentialsComponent
    fun buildStepComponent(): StepComponent
    fun buildStepQuizComponent(): StepQuizComponent
    fun buildProfileDataComponent(): ProfileDataComponent
}