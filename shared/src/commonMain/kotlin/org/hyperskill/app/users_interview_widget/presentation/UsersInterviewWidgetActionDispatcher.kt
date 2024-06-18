package org.hyperskill.app.users_interview_widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Action
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature.Message

class UsersInterviewWidgetActionDispatcher internal constructor(
    mainUsersInterviewWidgetActionDispatcher: MainUsersInterviewWidgetActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainUsersInterviewWidgetActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? UsersInterviewWidgetFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)