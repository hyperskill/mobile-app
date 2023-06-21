package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformTrackComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}