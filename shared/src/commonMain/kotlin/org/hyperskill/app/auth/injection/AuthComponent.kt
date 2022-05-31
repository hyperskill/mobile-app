package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.presentation.AuthSocialFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface AuthComponent {
    val authInteractor: AuthInteractor
}