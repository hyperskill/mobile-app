package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformProblemsLimitComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}