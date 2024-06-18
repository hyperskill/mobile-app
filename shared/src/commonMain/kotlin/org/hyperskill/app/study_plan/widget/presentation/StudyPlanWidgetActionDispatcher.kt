package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.SingleAnalyticEventActionDispatcher
import org.hyperskill.app.core.presentation.CompositeActionDispatcher
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message

class StudyPlanWidgetActionDispatcher internal constructor(
    mainStudyPlanWidgetActionDispatcher: MainStudyPlanWidgetActionDispatcher,
    analyticInteractor: AnalyticInteractor
) : CompositeActionDispatcher<Action, Message>(
    listOf(
        mainStudyPlanWidgetActionDispatcher,
        SingleAnalyticEventActionDispatcher(analyticInteractor) {
            (it as? StudyPlanWidgetFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
        }
    )
)