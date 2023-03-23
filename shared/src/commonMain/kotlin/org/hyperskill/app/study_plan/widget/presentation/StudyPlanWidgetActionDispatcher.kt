package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Action
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.InternalAction
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.Message
import org.hyperskill.app.track.domain.interactor.TrackInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StudyPlanWidgetActionDispatcher(
    config: ActionDispatcherOptions,
    private val studyPlanInteractor: StudyPlanInteractor,
    private val trackInteractor: TrackInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            InternalAction.FetchStudyPlan -> {
                studyPlanInteractor.getCurrentStudyPlan()
                    .onSuccess { studyPlan ->
                        onNewMessage(StudyPlanWidgetFeature.StudyPlanFetchResult.Success(studyPlan))
                    }
                    .onFailure {
                        onNewMessage(StudyPlanWidgetFeature.StudyPlanFetchResult.Failed)
                    }
            }
            is InternalAction.FetchSections -> {
                studyPlanInteractor.getStudyPlanSections(action.sectionsIds)
                    .onSuccess { sections ->
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Success(sections))
                    }
                    .onFailure {
                        onNewMessage(StudyPlanWidgetFeature.SectionsFetchResult.Failed)
                    }
            }
            is InternalAction.FetchActivities -> {
                studyPlanInteractor.getLearningActivities(action.activitiesIds, action.types, action.states)
                    .onSuccess { learningActivities ->
                        onNewMessage(
                            StudyPlanWidgetFeature.LearningActivitiesFetchResult.Success(
                                action.sectionId,
                                learningActivities
                            )
                        )
                    }
                    .onFailure {
                        onNewMessage(StudyPlanWidgetFeature.LearningActivitiesFetchResult.Failed(action.sectionId))
                    }
            }
            is InternalAction.FetchTrack -> {
                trackInteractor.getTrack(action.trackId)
                    .onSuccess {
                        onNewMessage(
                            StudyPlanWidgetFeature.TrackFetchResult.Success(it)
                        )
                    }
                    .onFailure {
                        onNewMessage(StudyPlanWidgetFeature.TrackFetchResult.Failed)
                    }
            }
            else -> {
                // no op
            }
        }
    }
}