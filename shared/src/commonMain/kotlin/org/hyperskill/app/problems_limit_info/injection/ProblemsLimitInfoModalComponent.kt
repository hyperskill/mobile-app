package org.hyperskill.app.problems_limit_info.injection

import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Message
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface ProblemsLimitInfoModalComponent {
    val problemsLimitInfoModalFeature: Feature<ViewState, Message, Action>
}