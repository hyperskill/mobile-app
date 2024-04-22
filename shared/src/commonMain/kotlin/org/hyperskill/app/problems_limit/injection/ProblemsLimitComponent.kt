package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitActionDispatcher
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer

interface ProblemsLimitComponent {
    val problemsLimitReducer: ProblemsLimitReducer
    val problemsLimitActionDispatcher: ProblemsLimitActionDispatcher
}