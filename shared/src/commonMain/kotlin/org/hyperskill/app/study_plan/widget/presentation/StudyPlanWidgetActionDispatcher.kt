package org.hyperskill.app.study_plan.widget.presentation

import kotlinx.coroutines.delay
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
            is InternalAction.FetchStudyPlan -> {
                if (action.delayBeforeFetching != null) {
                    delay(action.delayBeforeFetching)
                }
                studyPlanInteractor.getCurrentStudyPlan(forceLoadFromRemote = true)
                    .onSuccess { studyPlan ->
                        onNewMessage(
                            StudyPlanWidgetFeature.StudyPlanFetchResult.Success(
                                studyPlan = studyPlan,
                                attemptNumber = action.attemptNumber,
                                showLoadingIndicators = action.showLoadingIndicators
                            )
                        )
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
                        learningActivities.forEach { learningActivity ->
                            learningActivity.sectionId = action.sectionId
                        }
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
                trackInteractor.getTrack(action.trackId, true)
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