package org.hyperskill.app.step.data.source

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepContext

interface StepRemoteDataSource {
    suspend fun getSteps(stepIds: List<Long>): Result<List<Step>>

    suspend fun completeStep(stepId: Long): Result<Step>

    suspend fun skipStep(stepId: Long): Result<Step>

    suspend fun getRecommendedStepsByTopicId(topicId: Long): Result<List<Step>>

    suspend fun viewStep(stepId: Long, stepContext: StepContext)
}