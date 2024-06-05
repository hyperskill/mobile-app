package org.hyperskill.app.step.data.repository

import kotlin.time.Duration
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext
import org.hyperskill.app.step.domain.repository.StepRepository

class StepRepositoryImpl(
    private val stepRemoteDataSource: StepRemoteDataSource
) : StepRepository {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        stepRemoteDataSource.getSteps(stepIds)

    override suspend fun completeStep(stepId: Long): Result<Step> =
        stepRemoteDataSource.completeStep(stepId)

    override suspend fun skipStep(stepId: Long): Result<Unit> =
        stepRemoteDataSource.skipStep(stepId)

    override suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>> =
        stepRemoteDataSource.getRecommendedStepsByTopicId(topicId)

    override suspend fun viewStep(stepId: Long, stepContext: StepContext) {
        stepRemoteDataSource.viewStep(stepId, stepContext)
    }

    override suspend fun logStepSolvingTime(stepId: Long, duration: Duration): Result<Unit> =
        stepRemoteDataSource.logStepSolvingTime(
            stepId = stepId,
            seconds = duration.inWholeSeconds
        )
}