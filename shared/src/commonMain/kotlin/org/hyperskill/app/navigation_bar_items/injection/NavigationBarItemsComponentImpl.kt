package org.hyperskill.app.navigation_bar_items.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsActionDispatcher
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsReducer

class NavigationBarItemsComponentImpl(private val appGraph: AppGraph) : NavigationBarItemsComponent {
    override val navigationBarItemsReducer: NavigationBarItemsReducer
        get() = NavigationBarItemsReducer()

    override val navigationBarItemsActionDispatcher: NavigationBarItemsActionDispatcher
        get() = NavigationBarItemsActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.buildStreaksDataComponent().streaksInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}