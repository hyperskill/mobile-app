package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface AuthComponentManual {
    val authInteractor: AuthInteractor
    val authCredentialsFeature: Feature<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action>
    val authSocialFeature: Feature<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action>
}