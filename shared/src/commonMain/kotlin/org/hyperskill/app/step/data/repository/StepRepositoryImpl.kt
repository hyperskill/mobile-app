package org.hyperskill.app.step.data.repository

import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.repository.StepRepository

class StepRepositoryImpl(
    private val stepRemoteDataSource: StepRemoteDataSource
) : StepRepository {
    override suspend fun getSteps(stepIds: List<Long>): Result<List<Step>> =
        stepRemoteDataSource.getSteps(stepIds)

    override suspend fun completeStep(stepId: Long): Result<Step> =
        stepRemoteDataSource.completeStep(stepId)

    override suspend fun skipStep(stepId: Long): Result<Step> =
        stepRemoteDataSource.skipStep(stepId)

    override suspend fun getNextRecommendedStepByTopicId(topicId: Long): Result<Step> =
        kotlin.runCatching {
            stepRemoteDataSource.getRecommendedStepsByTopicId(topicId).getOrThrow().first { it.isNext }
        }
}