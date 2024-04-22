package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature

interface ProblemsLimitHost {
    fun render(viewState: ProblemsLimitFeature.ViewState)
}