package org.hyperskill.app.step.domain.repository

import org.hyperskill.app.step.domain.model.Step

interface StepRepository {
    suspend fun getStep(stepId: Long): Result<Step> =
        getSteps(listOf(stepId)).map { it.first() }

    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>>

    suspend fun completeStep(stepId: Long): Result<Step>

    suspend fun getNextRecommendedStepByTopicId(topicId: Long): Result<Step>
}