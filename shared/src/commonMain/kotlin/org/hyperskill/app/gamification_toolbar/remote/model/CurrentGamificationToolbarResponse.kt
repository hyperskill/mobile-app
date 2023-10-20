package org.hyperskill.app.gamification_toolbar.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarData

@Serializable
class CurrentGamificationToolbarResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("gamification-toolbars")
    val gamificationToolbars: List<GamificationToolbarData>
) : MetaResponse