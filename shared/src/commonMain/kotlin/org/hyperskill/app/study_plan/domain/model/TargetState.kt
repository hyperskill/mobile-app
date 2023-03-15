package org.hyperskill.app.study_plan.domain.model

import kotlinx.serialization.Serializable

@Serializable(with = TargetStateSerializer::class)
enum class TargetState(val intValue: Int) {
    TODO(1),
    SKIPPED(2),
    COMPLETED(3);

    companion object
}

internal fun TargetState.Companion.valueOf(intValue: Int): TargetState =
    TargetState.values().first { it.intValue == intValue }