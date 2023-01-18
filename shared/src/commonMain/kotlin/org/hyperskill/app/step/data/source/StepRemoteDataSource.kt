package org.hyperskill.app.step.data.source

import org.hyperskill.app.step.domain.model.Step

interface StepRemoteDataSource {
    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>>

    suspend fun completeStep(stepId: Long): Result<Step>

    suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>>
}