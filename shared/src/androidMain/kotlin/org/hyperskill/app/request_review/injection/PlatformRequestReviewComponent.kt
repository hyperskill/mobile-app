package org.hyperskill.app.request_review.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformRequestReviewComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}