package org.hyperskill.app.comments.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformCommentsComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}