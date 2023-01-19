package org.hyperskill.app.profile.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.streaks.domain.model.Streak

class ProfileStreakDataComponentImpl : ProfileStreakDataComponent {
    override val streakMutableSharedFlow: MutableSharedFlow<Streak?> = MutableSharedFlow()
}