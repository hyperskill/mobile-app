package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.SavedStateReduxViewModelFactory

actual interface PlatformMainComponent {
    val reduxViewModelFactory: SavedStateReduxViewModelFactory
}