package org.hyperskill.app.progress.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformProgressScreenComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}