package org.hyperskill.streak_recovery

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryReducer
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.streaks.domain.model.stub

class StreakRecoveryTest {
    private val streakRecoveryReducer = StreakRecoveryReducer()

    @Test
    fun `Streak recovery modal should be shown if recovery is available`() {
        val (_, actions) = streakRecoveryReducer.reduce(
            StreakRecoveryFeature.State(),
            StreakRecoveryFeature.FetchStreakResult.Success(Streak.stub(canBeRecovered = true), "", "", "")
        )
        assertTrue {
            actions.any {
                it is StreakRecoveryFeature.Action.ViewAction.ShowRecoveryStreakModal
            }
        }
    }

    @Test
    fun `Streak recovery modal should NOT be shown if recovery is NOT available`() {
        val (_, actions) = streakRecoveryReducer.reduce(
            StreakRecoveryFeature.State(),
            StreakRecoveryFeature.FetchStreakResult.Success(Streak.stub(canBeRecovered = false), "", "", "")
        )
        assertTrue {
            actions.none {
                it is StreakRecoveryFeature.Action.ViewAction.ShowRecoveryStreakModal
            }
        }
    }

    @Test
    fun `Recover streak action should be dispatched if 'Restore streak' clicked`() {
        val (_, actions) = streakRecoveryReducer.reduce(
            StreakRecoveryFeature.State(Streak.stub(canBeRecovered = true)),
            StreakRecoveryFeature.Message.RestoreStreakClicked
        )
        assertTrue {
            actions.any {
                it is StreakRecoveryFeature.InternalAction.RecoverStreak
            }
        }
    }

    @Test
    fun `Cancel streak recovery action should be dispatched if 'No thanks' clicked`() {
        val (_, actions) = streakRecoveryReducer.reduce(
            StreakRecoveryFeature.State(),
            StreakRecoveryFeature.Message.NoThanksClicked
        )
        assertTrue {
            actions.any {
                it is StreakRecoveryFeature.InternalAction.CancelStreakRecovery
            }
        }
    }
}