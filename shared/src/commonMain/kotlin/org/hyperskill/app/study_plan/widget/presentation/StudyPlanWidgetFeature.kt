package org.hyperskill.app.study_plan.widget.presentation

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.widget.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.track.domain.model.Track

object StudyPlanWidgetFeature {
    internal val STUDY_PLAN_FETCH_INTERVAL: Duration = 1.seconds

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
        val activities: Map<Long, LearningActivity> = emptyMap(),

        /**
         * Pull to refresh flag
         */
        val isRefreshing: Boolean = false
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
        data class Initialize(val forceUpdate: Boolean = false) : Message

        data class SectionClicked(val sectionId: Long) : Message

        data class ActivityClicked(val activityId: Long) : Message

        data class RetryActivitiesLoading(val sectionId: Long) : Message

        object ReloadContentInBackground : Message

        object PullToRefresh : Message

        /**
         * Stage implementation unsupported modal
         */
        object StageImplementUnsupportedModalGoToHomeClicked : Message
        object StageImplementUnsupportedModalShownEventMessage : Message
        object StageImplementUnsupportedModalHiddenEventMessage : Message
    }

    internal sealed interface StudyPlanFetchResult : Message {
        data class Success(
            val studyPlan: StudyPlan,
            val attemptNumber: Int,
            val showLoadingIndicators: Boolean
        ) : StudyPlanFetchResult

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
                object Home : NavigateTo
                data class LearningActivityTarget(val viewAction: LearningActivityTargetViewAction) : NavigateTo
            }
        }
    }

    internal sealed interface InternalAction : Action {
        /**
         * Triggers a study plan fetching.
         * @param [delayBeforeFetching] is used to wait for definite duration before fetching.
         * @param [attemptNumber] represents the number of current attempt of the StudyPlan fetching.
         * [attemptNumber] should be passed back in the [StudyPlanFetchResult.Success.attemptNumber].
         */
        data class FetchStudyPlan(
            val delayBeforeFetching: Duration? = null,
            val attemptNumber: Int = 1,
            val showLoadingIndicators: Boolean = true
        ) : InternalAction

        data class FetchSections(val sectionsIds: List<Long>) : InternalAction

        data class FetchActivities(
            val sectionId: Long,
            val activitiesIds: List<Long>,
            val types: Set<LearningActivityType> = LearningActivityType.supportedTypes(),
            val states: Set<LearningActivityState> = setOf(LearningActivityState.TODO),
            val sentryTransaction: HyperskillSentryTransaction
        ) : InternalAction

        data class FetchTrack(val trackId: Long) : InternalAction

        data class UpdateNextLearningActivityState(val learningActivity: LearningActivity?) : InternalAction

        data class CaptureSentryException(val throwable: Throwable) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}