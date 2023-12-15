package org.hyperskill.app.search.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformSearchComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}