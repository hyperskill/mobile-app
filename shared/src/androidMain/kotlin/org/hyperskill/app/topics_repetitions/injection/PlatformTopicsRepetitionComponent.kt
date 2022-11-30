package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformTopicsRepetitionComponent {
    val reduxViewModel: ReduxViewModelFactory
}