package org.hyperskill.app.android.gamification_toolbar.view.ui.delegate

import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature

class GamificationToolbarDelegate(
    lifecycleOwner: LifecycleOwner,
    private val viewBinding: LayoutGamificationToolbarBinding,
    onNewMessage: (GamificationToolbarFeature.Message) -> Unit
) {

    init {
        with(viewBinding) {
            gamificationAppBar.setElevationOnCollapsed(lifecycleOwner.lifecycle)
            gamificationAppBar.setExpanded(true)

            gamificationGemsCountTextView.setOnClickListener {
                onNewMessage(
                    GamificationToolbarFeature.Message.ClickedGems
                )
            }
            gamificationStreakDurationTextView.setOnClickListener {
                onNewMessage(
                    GamificationToolbarFeature.Message.ClickedStreak
                )
            }
        }
    }

    fun render(state: GamificationToolbarFeature.State) {
        if (state is GamificationToolbarFeature.State.Content) {
            with(viewBinding.gamificationStreakDurationTextView) {
                isVisible = true
                val streakDuration = state.streak?.currentStreak ?: 0
                text = streakDuration.toString()
                setCompoundDrawablesWithIntrinsicBounds(
                    /* left = */ if (state.streak?.history?.firstOrNull()?.isCompleted == true) {
                        R.drawable.ic_menu_streak
                    } else {
                        R.drawable.ic_menu_empty_streak
                    },
                    /* top = */ 0,
                    /* right = */ 0,
                    /* bottom = */ 0
                )
            }
            with(viewBinding.gamificationGemsCountTextView) {
                isVisible = state.trackWithProgress != null
                text = state.trackWithProgress?.averageProgress?.toString()
            }

            state.trackWithProgress.let { trackProgress ->
                viewBinding.gamificationTrackProgressLinearLayout.isVisible = trackProgress != null
                if (trackProgress != null) {
                    viewBinding.gamificationTrackProgressView.setProgress(
                        trackProgress.averageProgress,
                        trackProgress.trackProgress.isCompleted
                    )
                }
            }
        }
    }
}