package org.hyperskill.app.android.gamification_toolbar.view.ui.delegate

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import com.github.terrakok.cicerone.Router
import com.google.android.material.appbar.AppBarLayout
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.streaks.domain.model.StreakState
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class GamificationToolbarDelegate(
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
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
            gamificationTrackProgressLinearLayout.setOnClickListener {
                onNewMessage(
                    GamificationToolbarFeature.Message.ClickedProgress
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
                val historicalStreak = state.streak?.history?.firstOrNull()
                setCompoundDrawablesWithIntrinsicBounds(
                    /* left = */ when {
                        historicalStreak?.state == StreakState.RECOVERED -> R.drawable.ic_menu_recovered_streak
                        historicalStreak?.isCompleted == true -> R.drawable.ic_menu_enabled_streak
                        else -> R.drawable.ic_menu_empty_streak
                    },
                    /* top = */ 0,
                    /* right = */ 0,
                    /* bottom = */ 0
                )
            }
            with(viewBinding.gamificationGemsCountTextView) {
                isVisible = true
                text = state.hypercoinsBalance.toString()
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

    fun onAction(
        action: GamificationToolbarFeature.Action.ViewAction,
        mainScreenRouter: MainScreenRouter,
        router: Router
    ) {
        when (action) {
            is GamificationToolbarFeature.Action.ViewAction.ShowProfileTab ->
                mainScreenRouter.switch(ProfileScreen(isInitCurrent = true))
            GamificationToolbarFeature.Action.ViewAction.NavigateTo.ProgressScreen ->
                router.navigateToProgressScreen()
        }
    }

    fun setSubtitle(subtitle: String?) {
        with(viewBinding.subtitle) {
            isVisible = subtitle != null
            if (subtitle != null) {
                setTextIfChanged(subtitle)
            }
        }
        viewBinding.gamificationCollapsingToolbarLayout.updateLayoutParams<AppBarLayout.LayoutParams> {
            height = context.resources.getDimensionPixelOffset(
                if (subtitle != null) {
                    R.dimen.gamification_toolbar_with_subtitle_height
                } else {
                    R.dimen.gamification_toolbar_default_height
                }
            )
        }
        viewBinding.gamificationCollapsingToolbarLayout.expandedTitleMarginBottom =
            context.resources.getDimensionPixelOffset(
                if (subtitle != null) {
                    R.dimen.gamification_toolbar_with_subtitle_expanded_title_margin_bottom
                } else {
                    R.dimen.gamification_toolbar_default_expanded_title_margin_bottom
                }
            )
    }
}