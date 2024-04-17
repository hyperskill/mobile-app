package org.hyperskill.app.problems_limit_reached.injection

import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProblemsLimitReachedModalComponent {
    val problemsLimitReachedModalFeature: Feature<ViewState, Message, Action>
}