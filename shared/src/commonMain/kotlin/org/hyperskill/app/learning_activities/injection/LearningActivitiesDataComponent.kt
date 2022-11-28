package org.hyperskill.app.learning_activities.injection

import org.hyperskill.app.learning_activities.domain.interactor.LearningActivitiesInteractor

interface LearningActivitiesDataComponent {
    val learningActivitiesInteractor: LearningActivitiesInteractor
}