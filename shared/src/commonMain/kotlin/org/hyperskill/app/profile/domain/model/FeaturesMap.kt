package org.hyperskill.app.profile.domain.model

data class FeaturesMap(private val origin: Map<String, Boolean>) : Map<String, Boolean> by origin

val FeaturesMap.isRecommendationsJavaProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_JAVA_PROJECTS) ?: false

val FeaturesMap.isRecommendationsKotlinProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_KOTLIN_PROJECTS) ?: false

val FeaturesMap.isRecommendationsPythonProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_PYTHON_PROJECTS) ?: false

val FeaturesMap.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled: Boolean
    get() = get(FeatureKeys.FREEMIUM_INCREASE_LIMITS_FOR_FIRST_STEP_COMPLETION) ?: false

val FeaturesMap.isLearningPathDividedTrackTopicsEnabled: Boolean
    get() = get(FeatureKeys.LEARNING_PATH_DIVIDED_TRACK_TOPICS) ?: false

val FeaturesMap.isMobileLeaderboardsEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_LEADERBOARDS) ?: false

val FeaturesMap.isMobileInterviewPreparationEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_INTERVIEW_PREPARATION) ?: false