package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Action
import org.hyperskill.app.study_plan.presentation.StudyPlanFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanActionDispatcher(
    config: ActionDispatcherOptions,
    private val studyPlanInteractor: StudyPlanInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            Action.FetchStudyPlan -> {
                studyPlanInteractor.getCurrentStudyPlan()
                    .onSuccess { studyPlan ->
                        onNewMessage(StudyPlanFeature.StudyPlanFetchResult.Success(studyPlan))
                    }
                    .onFailure {
                        onNewMessage(StudyPlanFeature.StudyPlanFetchResult.Failed)
                    }
            }
            is Action.FetchSections -> {
                studyPlanInteractor.getStudyPlanSections(action.sectionsIds)
                    .onSuccess { sections ->
                        onNewMessage(StudyPlanFeature.SectionsFetchResult.Success(sections))
                    }
                    .onFailure {
                        onNewMessage(StudyPlanFeature.SectionsFetchResult.Failed)
                    }
            }
            is Action.FetchActivities -> {
                studyPlanInteractor.getLearningActivities(action.activitiesIds, action.types)
                    .onSuccess { learningActivities ->
                        onNewMessage(
                            StudyPlanFeature.LearningActivitiesFetchResult.Success(action.sectionId, learningActivities)
                        )
                    }
                    .onFailure {
                        onNewMessage(StudyPlanFeature.LearningActivitiesFetchResult.Failed(action.sectionId))
                    }
            }
        }
    }
}