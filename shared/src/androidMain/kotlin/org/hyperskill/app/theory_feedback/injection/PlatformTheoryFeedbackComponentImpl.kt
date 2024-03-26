package org.hyperskill.app.theory_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTheoryFeedbackComponentImpl(
    private val theoryFeedbackComponent: TheoryFeedbackComponent
) : PlatformTheoryFeedbackComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory =
        ReduxViewModelFactory(
            mapOf(
                TheoryFeedbackViewModel::class.java to {
                    TheoryFeedbackViewModel(
                        theoryFeedbackComponent.theoryFeedbackFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}