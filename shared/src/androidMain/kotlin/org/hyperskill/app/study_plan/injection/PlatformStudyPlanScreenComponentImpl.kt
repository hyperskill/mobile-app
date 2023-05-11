package org.hyperskill.app.study_plan.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.study_plan.presentation.StudyPlanScreenViewModel
import org.hyperskill.app.study_plan.screen.injection.StudyPlanScreenComponent
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStudyPlanScreenComponentImpl(
    private val studyPlanScreenComponent: StudyPlanScreenComponent
) : PlatformStudyPlanScreenComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                StudyPlanScreenViewModel::class.java to {
                    StudyPlanScreenViewModel(
                        reduxViewContainer = studyPlanScreenComponent.studyPlanScreenFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}