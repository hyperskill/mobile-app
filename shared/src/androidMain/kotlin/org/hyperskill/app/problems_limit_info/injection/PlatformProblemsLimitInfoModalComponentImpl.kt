package org.hyperskill.app.problems_limit_info.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProblemsLimitInfoModalComponentImpl(
    private val problemsLimitInfoModalComponent: ProblemsLimitInfoModalComponent
) : PlatformProblemsLimitInfoModalComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                ProblemsLimitInfoModalViewModel::class.java to {
                    ProblemsLimitInfoModalViewModel(
                        problemsLimitInfoModalComponent.problemsLimitInfoModalFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}