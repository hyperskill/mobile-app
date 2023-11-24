package org.hyperskill.app.learning_activities.injection

import org.hyperskill.app.learning_activities.domain.repository.LearningActivitiesRepository

interface LearningActivitiesDataComponent {
    val learningActivitiesRepository: LearningActivitiesRepository
}