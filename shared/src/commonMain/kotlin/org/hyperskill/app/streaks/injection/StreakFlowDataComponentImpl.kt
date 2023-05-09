package org.hyperskill.app.streaks.injection

import org.hyperskill.app.streaks.data.flow.StreakFlowImpl
import org.hyperskill.app.streaks.domain.flow.StreakFlow

class StreakFlowDataComponentImpl : StreakFlowDataComponent {
    override val streakFlow: StreakFlow = StreakFlowImpl()
}