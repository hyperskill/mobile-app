package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isLearningPathDividedTrackTopicsEnabled
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType

object StudyPlanWidgetFeature {
    data class State(
        val profile: Profile? = null,

        val studyPlanSections: Map<Long, StudyPlanSectionInfo> = emptyMap(),

        /**
         * Describes status of sections loading
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
    ) {
        /**
         * Divided track topics feature enabled flag
         */
        val isLearningPathDividedTrackTopicsEnabled: Boolean
            get() = profile?.features?.isLearningPathDividedTrackTopicsEnabled ?: false
    }

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
        data class SectionClicked(val sectionId: Long) : Message

        data class ActivityClicked(val activityId: Long) : Message

        data class RetryActivitiesLoading(val sectionId: Long) : Message

        object PullToRefresh : Message

        /**
         * Stage implementation unsupported modal
         */
        object StageImplementUnsupportedModalGoToHomeClicked : Message
        object StageImplementUnsupportedModalShownEventMessage : Message
        object StageImplementUnsupportedModalHiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        object ReloadContentInBackground : InternalMessage

        object FetchLearningActivitiesWithSectionsFailed : InternalMessage
        data class FetchLearningActivitiesWithSectionsSuccess(
            val learningActivities: List<LearningActivity>,
            val studyPlanSections: List<StudyPlanSection>
        ) : InternalMessage

        data class ProfileChanged(val profile: Profile) : InternalMessage
    }

    internal sealed interface LearningActivitiesFetchResult : Message {
        data class Success(
            val sectionId: Long,
            val activities: List<LearningActivity>
        ) : LearningActivitiesFetchResult

        data class Failed(val sectionId: Long) : LearningActivitiesFetchResult
    }

    internal sealed interface ProfileFetchResult : Message {
        data class Success(val profile: Profile) : ProfileFetchResult

        object Failed : ProfileFetchResult
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
        data class FetchLearningActivitiesWithSections(
            val studyPlanSectionTypes: Set<StudyPlanSectionType> = StudyPlanSectionType.supportedTypes(),
            val learningActivityTypes: Set<LearningActivityType> = LearningActivityType.supportedTypes(),
            val learningActivityStates: Set<LearningActivityState> = setOf(LearningActivityState.TODO)
        ) : InternalAction

        data class FetchLearningActivities(
            val sectionId: Long,
            val activitiesIds: List<Long>,
            val types: Set<LearningActivityType> = LearningActivityType.supportedTypes(),
            val states: Set<LearningActivityState> = setOf(LearningActivityState.TODO),
            val sentryTransaction: HyperskillSentryTransaction
        ) : InternalAction

        object FetchProfile : InternalAction

        data class UpdateCurrentStudyPlanState(val forceUpdate: Boolean) : InternalAction
        data class UpdateNextLearningActivityState(val learningActivity: LearningActivity?) : InternalAction

        data class CaptureSentryException(val throwable: Throwable) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}