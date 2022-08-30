package org.hyperskill.app.analytic.domain.interactor

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.model.Profile

class AnalyticInteractor(
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    private val hyperskillRepository: AnalyticHyperskillRepository,
    private val hyperskillEventProcessor: AnalyticHyperskillEventProcessor
) : Analytic {
    companion object {
        private val FLUSH_EVENTS_DELAY_DURATION: Duration = 5.seconds
    }

    private var flushEventsJob: Job? = null

    override fun reportEvent(event: AnalyticEvent) {
        MainScope().launch {
            logEvent(event)
        }
    }

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

            val currentProfile = getCurrentProfile()
                .getOrElse { return }

            val processedEvent = hyperskillEventProcessor.processEvent(event, currentProfile.id)
            hyperskillRepository.logEvent(processedEvent)

            val isCurrentUserAuthorized = authInteractor
                .isAuthorized()
                .getOrDefault(false)
            if (!isCurrentUserAuthorized) {
                println("")
                println("*********************************************************************************")
                println("AnalyticInteractor :: unauthorized user skipping event = ${processedEvent.params}")
                println("*********************************************************************************")
                println("")
                flushEventsJob?.cancel()
                flushEventsJob = null
                return
            }

            if (flushEventsJob != null && !flushEventsJob!!.isCompleted) {
                return
            }

            flushEventsJob = MainScope().launch {
                delay(FLUSH_EVENTS_DELAY_DURATION)
                hyperskillRepository.flushEvents()
            }
        }
    }

    private suspend fun getCurrentProfile(): Result<Profile> {
        val cachedCurrentProfile = profileInteractor
            .getCurrentProfile(sourceType = DataSourceType.CACHE)
        return if (cachedCurrentProfile.isSuccess) {
            cachedCurrentProfile
        } else {
            profileInteractor.getCurrentProfile(sourceType = DataSourceType.REMOTE)
        }
    }
}