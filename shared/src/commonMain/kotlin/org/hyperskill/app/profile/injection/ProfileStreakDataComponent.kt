package org.hyperskill.app.profile.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.streaks.domain.model.Streak

interface ProfileStreakDataComponent {
    val streakMutableSharedFlow: MutableSharedFlow<Streak?>
}