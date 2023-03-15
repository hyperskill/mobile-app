package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.Serializable

@Serializable(with = TargetTypeSerializer::class)
enum class TargetType(val intValue: Int) {
    SELECT_PROJECT(0),
    LEARN_TOPIC(10),
    IMPLEMENT_STAGE(20),
    SELECT_TRACK(100);

    companion object
}

internal fun TargetType.Companion.valueOf(intValue: Int): TargetType =
    TargetType.values().first { it.intValue == intValue }