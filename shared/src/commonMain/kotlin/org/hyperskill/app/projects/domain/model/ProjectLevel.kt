package org.hyperskill.app.projects.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represent project complexity level.
 * Warning: don't change the order of the elements. The UI is on the order.
 */
@Serializable
enum class ProjectLevel {
    @SerialName("easy")
    EASY,
    @SerialName("medium")
    MEDIUM,
    @SerialName("hard")
    HARD,
    @SerialName("nightmare")
    NIGHTMARE
}