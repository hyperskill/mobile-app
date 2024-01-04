package org.hyperskill.app.interview_steps.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.interview_steps.data.source.TrackInterviewStepsRemoteDataSource
import org.hyperskill.app.interview_steps.domain.repository.InterviewStepsStateRepository

internal class InterviewStepsStateRepositoryImpl(
    private val trackInterviewStepsRemoteDataSource: TrackInterviewStepsRemoteDataSource
) : InterviewStepsStateRepository, BaseStateRepository<List<Long>>() {
    override suspend fun loadState(): Result<List<Long>> =
        trackInterviewStepsRemoteDataSource
            .getTrackInterviewSteps(pageSize = 20, page = 1)
            .map { it.steps.map { step -> step.id } }
}