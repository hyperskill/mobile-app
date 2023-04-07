package org.hyperskill.app.android.gamification_toolbar.view.ui.delegate

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.appbar.AppBarLayout
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class GamificationToolbarDelegate(
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val viewBinding: LayoutGamificationToolbarBinding,
    screen: GamificationToolbarScreen,
    onNewMessage: (GamificationToolbarFeature.Message) -> Unit
) {

    init {
        with(viewBinding) {
            gamificationAppBar.setElevationOnCollapsed(lifecycleOwner.lifecycle)
            gamificationAppBar.setExpanded(true)

            gamificationGemsCountTextView.setOnClickListener {
                onNewMessage(
                    GamificationToolbarFeature.Message.ClickedGems(screen)
                )
            }
            gamificationStreakDurationTextView.setOnClickListener {
                onNewMessage(
                    GamificationToolbarFeature.Message.ClickedStreak(screen)
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
                    if (state.streak?.history?.firstOrNull()?.isCompleted == true) R.drawable.ic_menu_streak else R.drawable.ic_menu_empty_streak, // left
                    0,
                    0,
                    0
                )
            }
            with(viewBinding.gamificationGemsCountTextView) {
                isVisible = true
                text = state.hypercoinsBalance.toString()
            }
        } else {
            // TODO: Implement skeletons for loading and idle states
            with(viewBinding.gamificationStreakDurationTextView) {
                isVisible = false
            }
            with(viewBinding.gamificationGemsCountTextView) {
                isVisible = false
            }
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