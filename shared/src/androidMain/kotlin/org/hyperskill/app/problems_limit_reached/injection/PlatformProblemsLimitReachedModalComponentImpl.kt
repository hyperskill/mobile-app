package org.hyperskill.app.problems_limit_reached.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProblemsLimitReachedModalComponentImpl(
    private val problemsLimitReachedModalComponent: ProblemsLimitReachedModalComponent
) : PlatformProblemsLimitReachedModalComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                ProblemsLimitReachedModalViewModel::class.java to {
                    ProblemsLimitReachedModalViewModel(
                        problemsLimitReachedModalComponent.problemsLimitReachedModalFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}