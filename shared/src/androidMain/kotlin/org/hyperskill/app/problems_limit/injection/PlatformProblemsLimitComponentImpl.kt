package org.hyperskill.app.problems_limit.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProblemsLimitComponentImpl(
    private val problemsLimitComponent: ProblemsLimitComponent
) : PlatformProblemsLimitComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                ProblemsLimitViewModel::class.java to
                    {
                        ProblemsLimitViewModel(
                            problemsLimitComponent.problemsLimitFeature.wrapWithViewContainer()
                        )
                    }
            )
        )
}