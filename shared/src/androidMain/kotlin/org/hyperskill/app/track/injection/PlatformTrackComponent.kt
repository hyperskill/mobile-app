package org.hyperskill.app.track.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformTrackComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}