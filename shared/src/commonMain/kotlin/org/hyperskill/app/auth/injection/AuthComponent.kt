package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.domain.interactor.AuthInteractor

interface AuthComponent {
    val authInteractor: AuthInteractor
}