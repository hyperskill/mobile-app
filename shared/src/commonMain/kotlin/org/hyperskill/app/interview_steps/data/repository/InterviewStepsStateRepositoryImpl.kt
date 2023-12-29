package org.hyperskill.app.interview_steps.data.repository

import kotlin.random.Random
import kotlinx.coroutines.delay
import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository

class InterviewStepsStateRepositoryImpl : InterviewStepsStateRepository, BaseStateRepository<List<Long>>() {
    companion object {
        private val stubSteps: List<Long> =
            listOf(
                3745, // Choice
                1983, // Code
                1985, // Parsons
            )
    }
    override suspend fun loadState(): Result<List<Long>> {
        val delay = Random.nextLong(800, 2000)
        delay(delay)
        return Result.success(stubSteps)
    }
}