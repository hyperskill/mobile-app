package org.hyperskill.app.step.domain.repository

import org.hyperskill.app.step.domain.model.Step

interface StepRepository {
    suspend fun getStep(stepId: Long): Result<Step> =
        kotlin.runCatching {
            getSteps(listOf(stepId)).getOrThrow().first()
        }

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>>

    suspend fun completeStep(stepId: Long): Result<Step>

    suspend fun skipStep(stepId: Long): Result<Step>

    suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>>

    suspend fun getNextRecommendedStepByTopicId(topicId: Long): Result<Step> =
        kotlin.runCatching {
            getRecommendedStepsByTopicId(topicId).getOrThrow().first { it.isNext }
        }
}