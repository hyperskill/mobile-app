package org.hyperskill.app.topics_to_discover_next.domain.interactor

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository
import org.hyperskill.app.profile.domain.model.isCurrentTrackCompleted
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.study_plan.domain.interactor.StudyPlanInteractor
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.app.topics.domain.repository.TopicsRepository

class TopicsToDiscoverNextInteractor(
    private val profileRepository: ProfileRepository,
    private val learningActivitiesRepository: LearningActivitiesRepository,
    private val topicsRepository: TopicsRepository,
    private val progressesRepository: ProgressesRepository,
    private val studyPlanInteractor: StudyPlanInteractor
) {

    companion object {
        private const val LEARNING_ACTIVITIES_PAGE_SIZE = 20
        private const val TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT = 10
    }

    /**
     * Returns a topic to discover next.
     */
    suspend fun getNextTopicToDiscover(): Result<Topic?> =
        getTopicsToDiscoverNext().map { it.firstOrNull() }

    /**
     * Returns a list of topics to discover next.
     */
    suspend fun getTopicsToDiscoverNext(): Result<List<Topic>> =
        coroutineScope {
            kotlin.runCatching {
                val studyPlanResult = async { studyPlanInteractor.getCurrentStudyPlan() }
                val currentProfileResult = async { profileRepository.getCurrentProfile(primarySourceType = DataSourceType.CACHE) }

                val studyPlan = studyPlanResult.await().getOrNull()
                val currentProfile = currentProfileResult.await().getOrThrow()

                if (currentProfile.isCurrentTrackCompleted || studyPlan == null) {
                    return@runCatching emptyList()
                }

                val learningActivities = learningActivitiesRepository
                    .getUncompletedTopicsLearningActivities(
                        studyPlanId = studyPlan.id,
                        pageSize = LEARNING_ACTIVITIES_PAGE_SIZE
                    )
                    .map { it.learningActivities }
                    .getOrThrow()

                if (learningActivities.isEmpty()) {
                    return@runCatching emptyList()
                }

                val topicsIds = learningActivities.map { it.targetId }

                val topicsResult = async { topicsRepository.getTopics(topicsIds) }
                val topicsProgressesResult = async { progressesRepository.getTopicsProgresses(topicsIds) }
                val topics = topicsResult.await().getOrThrow()
                val topicsProgresses = topicsProgressesResult.await().getOrThrow()

                val topicProgressById = topicsProgresses.associateBy { it.id }
                val topicsWithCurrentTrackProgresses = topics
                    .map { it.copy(progress = topicProgressById[it.progressId]) }
                    .filter { it.progress?.isInCurrentTrack ?: false }

                val topicsByStagePosition = topicsWithCurrentTrackProgresses
                    .filter { it.progress?.stagePosition != null }
                    .groupBy { it.progress!!.stagePosition!! }

                val minStagePositionKey = topicsByStagePosition.keys.minOrNull()

                return@runCatching if (minStagePositionKey != null) {
                    topicsByStagePosition
                        .getValue(minStagePositionKey)
                        .take(TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT)
                } else {
                    topicsWithCurrentTrackProgresses.take(TOPICS_TO_DISCOVER_NEXT_PREFIX_COUNT)
                }
            }
        }
}