package org.hyperskill.app.profile.domain.model

data class FeaturesMap(internal val origin: Map<String, Boolean>) : Map<String, Boolean> by origin

val FeaturesMap.isRecommendationsJavaProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_JAVA_PROJECTS) ?: false

val FeaturesMap.isRecommendationsKotlinProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_KOTLIN_PROJECTS) ?: false

val FeaturesMap.isRecommendationsPythonProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_PYTHON_PROJECTS) ?: false

val FeaturesMap.isFreemiumIncreaseLimitsForFirstStepCompletionEnabled: Boolean
    get() = get(FeatureKeys.FREEMIUM_INCREASE_LIMITS_FOR_FIRST_STEP_COMPLETION) ?: false

val FeaturesMap.isFreemiumWrongSubmissionChargeLimitsEnabled: Boolean
    get() = get(FeatureKeys.FREEMIUM_WRONG_SUBMISSION_CHARGE_LIMITS) ?: false

val FeaturesMap.isLearningPathDividedTrackTopicsEnabled: Boolean
    get() = get(FeatureKeys.LEARNING_PATH_DIVIDED_TRACK_TOPICS) ?: false

val FeaturesMap.isMobileLeaderboardsEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_LEADERBOARDS) ?: false

val FeaturesMap.isMobileOnlySubscriptionEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_ONLY_SUBSCRIPTION) ?: false

val FeaturesMap.isMobileUsersInterviewWidgetEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_USERS_INTERVIEW_WIDGET) ?: false

val FeaturesMap.isMobileGptCodeGenerationWithErrorsEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_GPT_CODE_GENERATION_WITH_ERRORS_FORCED_ONBOARDING) ?: false

val FeaturesMap.isMobileContentTrialEnabled: Boolean
    get() = get(FeatureKeys.MOBILE_CONTENT_TRIAL) ?: false