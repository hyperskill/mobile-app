package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.Serializable

@Serializable(with = TargetTypeSerializer::class)
enum class LearningActivityType(val intValue: Int) {
    SELECT_PROJECT(0),
    LEARN_TOPIC(10),
    IMPLEMENT_STAGE(20),
    SELECT_TRACK(100);

    companion object
}

internal fun LearningActivityType.Companion.valueOf(intValue: Int): LearningActivityType =
    LearningActivityType.values().first { it.intValue == intValue }