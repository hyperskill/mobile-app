package org.hyperskill.app.welcome.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.welcome.presentation.WelcomeViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformWelcomeComponentImpl(
    private val welcomeComponent: WelcomeComponent
) : PlatformWelcomeComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                WelcomeViewModel::class.java to {
                    WelcomeViewModel(welcomeComponent.welcomeFeature.wrapWithViewContainer())
                }
            )
        )
}