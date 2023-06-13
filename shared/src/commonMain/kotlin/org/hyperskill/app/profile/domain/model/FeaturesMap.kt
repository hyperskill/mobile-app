package org.hyperskill.app.profile.domain.model

data class FeaturesMap(private val origin: Map<String, Boolean>) : Map<String, Boolean> by origin

val FeaturesMap.isRecommendationsJavaProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_JAVA_PROJECTS) ?: false

val FeaturesMap.isRecommendationsKotlinProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_KOTLIN_PROJECTS) ?: false

val FeaturesMap.isRecommendationsPythonProjectsFeatureEnabled: Boolean
    get() = get(FeatureKeys.RECOMMENDATIONS_PYTHON_PROJECTS) ?: false

val FeaturesMap.isStreakRecoveryFeatureEnabled: Boolean
    get() = get(FeatureKeys.USER_STREAK_RECOVER) ?: false