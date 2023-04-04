package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import ru.nobird.app.presentation.redux.feature.Feature

interface ProblemsLimitComponent {
    val problemsLimitFeature: Feature<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Message, ProblemsLimitFeature.Action>
}