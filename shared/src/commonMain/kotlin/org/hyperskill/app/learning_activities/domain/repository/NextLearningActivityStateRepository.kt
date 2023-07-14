package org.hyperskill.app.learning_activities.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.learning_activities.domain.model.LearningActivity

interface NextLearningActivityStateRepository : StateRepository<LearningActivity>