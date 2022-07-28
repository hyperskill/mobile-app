package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor

interface AuthComponent {
    val authInteractor: AuthInteractor
    val profileInteractor: ProfileInteractor
}