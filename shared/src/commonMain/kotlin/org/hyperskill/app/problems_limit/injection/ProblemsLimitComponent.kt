package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper

interface ProblemsLimitComponent {
    val problemsLimitReducer: ProblemsLimitReducer
    val problemsLimitActionDispatcher: ProblemsLimitActionDispatcher
    val problemsLimitViewStateMapper: ProblemsLimitViewStateMapper
}