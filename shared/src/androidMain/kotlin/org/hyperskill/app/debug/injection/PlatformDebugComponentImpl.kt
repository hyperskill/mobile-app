package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.debug.presentation.DebugViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformDebugComponentImpl(
    private val debugComponent: DebugComponent
) : PlatformDebugComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                DebugViewModel::class.java
                    to { DebugViewModel(debugComponent.debugFeature.wrapWithViewContainer()) }
            )
        )
}