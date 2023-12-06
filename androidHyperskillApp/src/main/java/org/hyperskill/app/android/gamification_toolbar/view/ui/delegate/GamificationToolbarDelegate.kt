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
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.progress.navigation.ProgressScreen
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
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

    fun render(state: GamificationToolbarFeature.ViewState) {
        if (state is GamificationToolbarFeature.ViewState.Content) {
            with(viewBinding.gamificationStreakDurationTextView) {
                isVisible = true
                text = state.streak.formattedValue
                setCompoundDrawablesWithIntrinsicBounds(
                    /* left = */ when {
                        state.streak.isRecovered -> R.drawable.ic_menu_recovered_streak
                        state.streak.isCompleted -> R.drawable.ic_menu_enabled_streak
                        else -> R.drawable.ic_menu_empty_streak
                    },
                    /* top = */ 0,
                    /* right = */ 0,
                    /* bottom = */ 0
                )
            }

            state.progress.let { progress ->
                viewBinding.gamificationTrackProgressLinearLayout.isVisible = progress != null
                if (progress != null) {
                    viewBinding.gamificationTrackProgressView.setProgress(
                        progress.value,
                        progress.isCompleted
                    )
                    viewBinding.gamificationTrackProgressTextView.text = progress.formattedValue
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
                mainScreenRouter.switch(Tabs.PROFILE)
            GamificationToolbarFeature.Action.ViewAction.ShowProgressScreen ->
                router.navigateTo(ProgressScreen)
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