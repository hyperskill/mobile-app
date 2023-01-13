package org.hyperskill.app.navigation_bar_items.injection

import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsActionDispatcher
import org.hyperskill.app.navigation_bar_items.presentation.NavigationBarItemsReducer

interface NavigationBarItemsComponent {
    val navigationBarItemsReducer: NavigationBarItemsReducer
    val navigationBarItemsActionDispatcher: NavigationBarItemsActionDispatcher
}