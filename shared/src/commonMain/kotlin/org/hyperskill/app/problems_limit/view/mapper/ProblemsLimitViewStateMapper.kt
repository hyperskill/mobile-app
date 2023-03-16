package org.hyperskill.app.problems_limit.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature

internal class ProblemsLimitViewStateMapper(
    private val resourceProvider: ResourceProvider,
    private val dateFormatter: DateFormatter
) {
    fun mapState(state: ProblemsLimitFeature.State): ProblemsLimitFeature.ViewState =
        when (state) {
            is ProblemsLimitFeature.State.Idle -> ProblemsLimitFeature.ViewState.Idle
            is ProblemsLimitFeature.State.Loading -> ProblemsLimitFeature.ViewState.Loading
            is ProblemsLimitFeature.State.NetworkError -> ProblemsLimitFeature.ViewState.Error
            is ProblemsLimitFeature.State.Content -> {
                when {
                    state.subscription.stepsLimitLeft == null ||
                        state.subscription.stepsLimitTotal == null ||
                        state.updateIn == null ->
                        ProblemsLimitFeature.ViewState.Content.Empty
                    else -> ProblemsLimitFeature.ViewState.Content.Widget(
                        stepsLimitTotal = state.subscription.stepsLimitTotal,
                        stepsLimitLeft = state.subscription.stepsLimitLeft,
                        stepsLimitLabel = resourceProvider.getString(
                            SharedResources.strings.problems_limit_widget_problems_limit,
                            state.subscription.stepsLimitLeft,
                            state.subscription.stepsLimitTotal
                        ),
                        updateInLabel = dateFormatter.hoursOrMinutesCount(state.updateIn)
                    )
                }
            }
        }
}