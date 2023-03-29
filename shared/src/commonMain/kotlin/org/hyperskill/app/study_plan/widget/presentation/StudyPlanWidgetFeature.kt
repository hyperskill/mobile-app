package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.track.domain.model.Track
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object StudyPlanWidgetFeature {

    val StudyPlanFetchInterval: Duration = 1.seconds

    data class State(
        val studyPlan: StudyPlan? = null,

        val track: Track? = null,

        val studyPlanSections: Map<Long, StudyPlanSectionInfo> = emptyMap(),

        /**
         * Describes status of sections loading, including [studyPlan] loading
         */
        val sectionsStatus: ContentStatus = ContentStatus.IDLE,

        /**
         * Map of activity ids to activities
         */
        val activities: Map<Long, LearningActivity> = emptyMap()
    )

    enum class ContentStatus {
        IDLE,
        LOADING,
        ERROR,
        LOADED
    }

    data class StudyPlanSectionInfo(
        val studyPlanSection: StudyPlanSection,
        val isExpanded: Boolean,

        /**
         * Describes status of section's activities loading
         * */
        val contentStatus: ContentStatus
    )

    sealed interface Message {
        object Initialize : Message

        data class SectionClicked(val sectionId: Long) : Message

        data class ActivityClicked(val activityId: Long) : Message

        object RetryContentLoading : Message
    }

    internal sealed interface StudyPlanFetchResult : Message {
        data class Success(val studyPlan: StudyPlan, val tryNumber: Int) : StudyPlanFetchResult

        object Failed : StudyPlanFetchResult
    }

    internal sealed interface SectionsFetchResult : Message {
        data class Success(val sections: List<StudyPlanSection>) : SectionsFetchResult

        object Failed : SectionsFetchResult
    }

    internal sealed interface LearningActivitiesFetchResult : Message {
        data class Success(
            val sectionId: Long,
            val activities: List<LearningActivity>
        ) : LearningActivitiesFetchResult

        data class Failed(val sectionId: Long) : LearningActivitiesFetchResult
    }

    internal sealed interface TrackFetchResult : Message {
        data class Success(val track: Track) : TrackFetchResult

        object Failed : TrackFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data class StageImplementation(
                    val stageId: Long,
                    val projectId: Long
                ) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        data class FetchStudyPlan(val delay: Duration? = null, val tryNumber: Int = 1) : InternalAction

        data class FetchSections(val sectionsIds: List<Long>) : InternalAction

        data class FetchActivities(
            val sectionId: Long,
            val activitiesIds: List<Long>,
            val types: Set<LearningActivityType> = LearningActivityType.supportedTypes(),
            val states: Set<LearningActivityState> = setOf(LearningActivityState.TODO)
        ) : InternalAction

        data class FetchTrack(val trackId: Long) : InternalAction
    }
}