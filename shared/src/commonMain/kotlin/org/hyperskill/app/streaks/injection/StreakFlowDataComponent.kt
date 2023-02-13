package org.hyperskill.app.streaks.injection

import org.hyperskill.app.streaks.domain.flow.StreakFlow

interface StreakFlowDataComponent {
    val streakFlow: StreakFlow
}