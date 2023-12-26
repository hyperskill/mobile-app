package org.hyperskill.app.interview_steps.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.step.domain.model.StepId

interface InterviewStepsStateRepository : StateRepository<List<StepId>>