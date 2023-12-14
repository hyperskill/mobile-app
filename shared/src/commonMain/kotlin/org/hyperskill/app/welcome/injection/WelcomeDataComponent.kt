package org.hyperskill.app.welcome.injection

import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor

interface WelcomeDataComponent {
    val welcomeInteractor: WelcomeInteractor
}