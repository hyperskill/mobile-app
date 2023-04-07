package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StudyPlanScreenViewModel(
    reduxViewContainer: ReduxViewContainer<StudyPlanScreenViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action.ViewAction>
) : ReduxViewModel<StudyPlanScreenViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(StudyPlanScreenFeature.Message.Initialize())
        onNewMessage(StudyPlanScreenFeature.Message.ViewedEventMessage)
    }
}