package org.hyperskill.app.home.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.home.presentation.HomeViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformHomeComponentImpl(private val homeComponent: HomeComponent) : PlatformHomeComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(mapOf(HomeViewModel::class.java to { HomeViewModel(homeComponent.homeFeature.wrapWithViewContainer()) }))
}