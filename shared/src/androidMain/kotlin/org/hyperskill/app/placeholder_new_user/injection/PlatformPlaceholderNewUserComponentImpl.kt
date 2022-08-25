package org.hyperskill.app.placeholder_new_user.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformPlaceholderNewUserComponentImpl(
    private val placeholderNewUserComponent: PlaceholderNewUserComponent
) : PlatformPlaceholderNewUserComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                PlaceholderNewUserViewModel::class.java
                    to { PlaceholderNewUserViewModel(placeholderNewUserComponent.placeholderNewUserFeature.wrapWithViewContainer()) }
            )
        )
}
