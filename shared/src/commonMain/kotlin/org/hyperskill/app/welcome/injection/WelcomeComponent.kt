package org.hyperskill.app.welcome.injection

import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor
import org.hyperskill.app.welcome.presentation.WelcomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface WelcomeComponent {
    val welcomeFeature: Feature<WelcomeFeature.State, WelcomeFeature.Message, WelcomeFeature.Action>

    val welcomeInteractor: WelcomeInteractor
}