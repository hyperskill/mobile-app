package org.hyperskill.app.analytic.domain.interactor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor

class AnalyticInteractor(
    private val profileInteractor: ProfileInteractor,
    private val hyperskillRepository: AnalyticHyperskillRepository,
    private val hyperskillEventProcessor: AnalyticHyperskillEventProcessor
) {
    suspend fun logEvent(event: AnalyticEvent) {
        when (event.source) {
            AnalyticSource.HYPERSKILL_API -> logHyperskillEvent(event)
        }
    }

    private suspend fun logHyperskillEvent(event: AnalyticEvent) {
        kotlin.runCatching {
            if (event !is HyperskillAnalyticEvent) {
                return
            }

            val currentProfile = profileInteractor
                .getCurrentProfile()
                .getOrElse { return }

            val processedEvent = hyperskillEventProcessor.processEvent(event, currentProfile.id)

            hyperskillRepository.logEvent(processedEvent)
            hyperskillRepository.flushEvents()
        }
    }
}