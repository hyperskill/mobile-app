package org.hyperskill.app.request_review.injection

import org.hyperskill.app.request_review.domain.interactor.RequestReviewInteractor

interface RequestReviewDataComponent {
    val requestReviewInteractor: RequestReviewInteractor
}