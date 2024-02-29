package org.hyperskill.app.request_review.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.request_review.cache.RequestReviewCacheDataSourceImpl
import org.hyperskill.app.request_review.data.repository.RequestReviewRepositoryImpl
import org.hyperskill.app.request_review.data.source.RequestReviewCacheDataSource
import org.hyperskill.app.request_review.domain.interactor.RequestReviewInteractor
import org.hyperskill.app.request_review.domain.repository.RequestReviewRepository

internal class RequestReviewDataComponentImpl(
    private val appGraph: AppGraph
) : RequestReviewDataComponent {
    private val requestReviewCacheDataSource: RequestReviewCacheDataSource =
        RequestReviewCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val requestReviewRepository: RequestReviewRepository =
        RequestReviewRepositoryImpl(requestReviewCacheDataSource)

    override val requestReviewInteractor: RequestReviewInteractor
        get() = RequestReviewInteractor(
            requestReviewRepository = requestReviewRepository,
            submissionRepository = appGraph.buildSubmissionDataComponent().submissionRepository
        )
}