package org.hyperskill.app.streaks.injection

import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.data.flow.StreakFlowImpl

class StreakFlowDataComponentImpl : StreakFlowDataComponent {
    override val streakFlow: StreakFlow = StreakFlowImpl()
}