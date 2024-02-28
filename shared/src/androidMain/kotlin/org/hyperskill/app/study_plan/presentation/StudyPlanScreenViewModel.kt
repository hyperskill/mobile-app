package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StudyPlanScreenViewModel(
    reduxViewContainer: ReduxViewContainer<
        StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Message, StudyPlanScreenFeature.Action.ViewAction>
) : ReduxViewModel<
    StudyPlanScreenFeature.ViewState,
    StudyPlanScreenFeature.Message,
    StudyPlanScreenFeature.Action.ViewAction>(reduxViewContainer) {

    init {
        onNewMessage(StudyPlanScreenFeature.Message.Initialize)
        onNewMessage(StudyPlanScreenFeature.Message.ViewedEventMessage)
    }

    fun onNewMessage(message: UsersQuestionnaireWidgetFeature.Message) {
        onNewMessage(StudyPlanScreenFeature.Message.UsersQuestionnaireWidgetMessage(message))
    }
}