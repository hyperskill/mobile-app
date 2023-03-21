package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ProblemsLimitFeatureBuilder {
    fun build(
        freemiumInteractor: FreemiumInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        resourceProvider: ResourceProvider,
        dateFormatter: DateFormatter
    ): Feature<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Message, ProblemsLimitFeature.Action> {
        val problemsLimitReducer = ProblemsLimitReducer()

        val problemsLimitDispatcher = ProblemsLimitActionDispatcher(
            ActionDispatcherOptions(),
            freemiumInteractor,
            currentSubscriptionStateRepository
        )

        val problemsLimitViewStateMapper = ProblemsLimitViewStateMapper(resourceProvider, dateFormatter)

        return ReduxFeature(ProblemsLimitFeature.State.Idle, problemsLimitReducer)
            .transformState(problemsLimitViewStateMapper::mapState)
            .wrapWithActionDispatcher(problemsLimitDispatcher)
    }
}