package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.model.isLearningPathDividedTrackTopicsEnabled
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionLimitType
import org.hyperskill.app.topics.domain.model.TopicProgress

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
        val isRefreshing: Boolean = false,

        /**
         * Actual learnedTopicsCount in the current track
         */
        val learnedTopicsCount: Int = 0,

        /**
         * Subscription limit type
         */
        val subscriptionLimitType: SubscriptionLimitType = SubscriptionLimitType.NONE
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

    enum class PageContentStatus {
        IDLE,
        AWAIT_LOADING,
        LOADING,
        ERROR,
        LOADED
    }

    enum class SectionPage {
        MAIN,
        NEXT,
        COMPLETED
    }

    data class StudyPlanSectionInfo(
        val studyPlanSection: StudyPlanSection,
        val isExpanded: Boolean,

        val mainPageContentStatus: ContentStatus,
        val nextPageContentStatus: PageContentStatus,
        val completedPageContentStatus: PageContentStatus
    )

    sealed interface Message {
        data class SectionClicked(val sectionId: Long) : Message

        data class ActivityClicked(val activityId: Long, val sectionId: Long) : Message

        data class LoadMoreActivitiesClicked(val sectionId: Long) : Message

        data class RetryActivitiesLoading(val sectionId: Long) : Message

        data class ExpandCompletedActivitiesClicked(val sectionId: Long) : Message

        data object PullToRefresh : Message

        data object SubscribeClicked : Message

        /**
         * Stage implementation unsupported modal
         */
        data object StageImplementUnsupportedModalGoToHomeClicked : Message
        data object StageImplementUnsupportedModalShownEventMessage : Message
        data object StageImplementUnsupportedModalHiddenEventMessage : Message
    }

    internal sealed interface InternalMessage : Message {
        data class Initialize(val forceUpdate: Boolean = false) : InternalMessage

        data object ReloadContentInBackground : InternalMessage

        data class ProfileChanged(val profile: Profile) : InternalMessage

        data class FetchSubscriptionLimitTypeResult(
            val subscriptionLimitType: SubscriptionLimitType
        ) : InternalMessage

        data class SubscriptionLimitTypeChanged(
            val subscriptionLimitType: SubscriptionLimitType
        ) : InternalMessage
    }

    internal sealed interface LearningActivitiesWithSectionsFetchResult : Message {
        data class Success(
            val learningActivities: List<LearningActivity>,
            val studyPlanSections: List<StudyPlanSection>,
            val learnedTopicsCount: Int,
            val subscription: Subscription,
            val subscriptionLimitType: SubscriptionLimitType
        ) : LearningActivitiesWithSectionsFetchResult

        data object Failed : LearningActivitiesWithSectionsFetchResult
    }

    internal sealed interface LearningActivitiesFetchResult : Message {
        data class Success(
            val sectionId: Long,
            val activities: List<LearningActivity>,
            val targetPage: SectionPage
        ) : LearningActivitiesFetchResult

        data class Failed(
            val sectionId: Long,
            val targetPage: SectionPage
        ) : LearningActivitiesFetchResult
    }

    internal sealed interface ProfileFetchResult : Message {
        data class Success(val profile: Profile) : ProfileFetchResult

        data object Failed : ProfileFetchResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            sealed interface NavigateTo : ViewAction {
                data object Home : NavigateTo
                data class LearningActivityTarget(val viewAction: LearningActivityTargetViewAction) : NavigateTo
                data class Paywall(val paywallTransitionSource: PaywallTransitionSource) : NavigateTo
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
            val states: Set<LearningActivityState> = setOf(
                LearningActivityState.TODO,
                LearningActivityState.COMPLETED,
                LearningActivityState.SKIPPED
            ),
            val sentryTransaction: HyperskillSentryTransaction,
            val targetPage: SectionPage
        ) : InternalAction

        data object FetchProfile : InternalAction

        data class UpdateCurrentStudyPlanState(val forceUpdate: Boolean) : InternalAction
        data class UpdateNextLearningActivityState(val learningActivity: LearningActivity?) : InternalAction

        data class PutTopicsProgressesToCache(val topicsProgresses: List<TopicProgress>) : InternalAction

        data class CaptureSentryException(val throwable: Throwable) : InternalAction
        data class LogAnalyticEvent(val analyticEvent: AnalyticEvent) : InternalAction
    }
}