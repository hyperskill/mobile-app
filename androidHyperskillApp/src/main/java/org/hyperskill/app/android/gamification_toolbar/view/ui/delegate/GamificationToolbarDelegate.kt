package org.hyperskill.app.android.gamification_toolbar.view.ui.delegate

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.github.terrakok.cicerone.Router
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.doOnApplyWindowInsets
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.problems_limit.dialog.ProblemsLimitInfoBottomSheet
import org.hyperskill.app.android.progress.navigation.ProgressScreen
import org.hyperskill.app.android.topic_search.navigation.TopicSearchScreen
import org.hyperskill.app.android.view.base.ui.extension.setCompoundDrawables
import org.hyperskill.app.android.view.base.ui.extension.setElevationOnCollapsed
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class GamificationToolbarDelegate(
    lifecycleOwner: LifecycleOwner,
    private val context: Context,
    private val viewBinding: LayoutGamificationToolbarBinding,
    onChangeTrackClicked: (() -> Unit)? = null,
    onNewMessage: (Message) -> Unit,
) {

    private var subtitle: String? = null

    init {
        with(viewBinding) {
            applyWindowInsets()
            gamificationAppBar.setElevationOnCollapsed(lifecycleOwner.lifecycle)
            gamificationAppBar.setExpanded(true)
            gamificationStreakDurationTextView.setOnClickListener {
                onNewMessage(Message.ClickedStreak)
            }
            gamificationTrackProgressLinearLayout.setOnClickListener {
                onNewMessage(Message.ClickedProgress)
            }
            gamificationSearchButton.setOnClickListener {
                onNewMessage(Message.ClickedSearch)
            }
            gamificationProblemsLimitTextView.setOnClickListener {
                onNewMessage(Message.ProblemsLimitClicked)
            }
            if (onChangeTrackClicked != null) {
                subtitleTextView.setOnClickListener {
                    onChangeTrackClicked()
                }
            }
        }
    }

    private fun applyWindowInsets() {
        var initialTopPadding = 0
        viewBinding.gamificationToolbar.doOnNextLayout {
            initialTopPadding = it.paddingTop
        }

        viewBinding.root.doOnApplyWindowInsets { _, insets, _ ->
            val insetTop = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            val toolbar = viewBinding.gamificationToolbar
            toolbar.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
                val typedValue = TypedValue()
                if (context.theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                    val toolbarHeight = TypedValue.complexToDimensionPixelSize(
                        /* data = */ typedValue.data,
                        /* metrics = */ context.resources.displayMetrics
                    )
                    height = toolbarHeight + insetTop
                }
            }

            toolbar.updatePadding(top = insetTop + initialTopPadding)

            applyInsetsToCollapsingToolbarLayout(
                context = context,
                collapsingToolbarLayout = viewBinding.gamificationCollapsingToolbarLayout,
                insetTop = insetTop,
                subtitle = subtitle
            )
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
                    viewBinding.gamificationTrackProgressTextView.setTextIfChanged(progress.formattedValue)
                }
            }
            state.problemsLimit.let { problemsLimit ->
                viewBinding.gamificationProblemsLimitTextView.isVisible = problemsLimit != null
                if (problemsLimit != null) {
                    viewBinding.gamificationProblemsLimitTextView.setTextIfChanged(problemsLimit.limitLabel)
                }
            }
            viewBinding.gamificationSearchButton.isVisible = true
        }
    }

    fun onAction(
        action: GamificationToolbarFeature.Action.ViewAction,
        mainScreenRouter: MainScreenRouter,
        router: Router,
        fragmentManager: FragmentManager
    ) {
        when (action) {
            is GamificationToolbarFeature.Action.ViewAction.ShowProfileTab ->
                mainScreenRouter.switch(Tabs.PROFILE)
            GamificationToolbarFeature.Action.ViewAction.ShowProgressScreen ->
                router.navigateTo(ProgressScreen)
            GamificationToolbarFeature.Action.ViewAction.ShowSearchScreen -> {
                router.navigateTo(TopicSearchScreen)
            }
            is GamificationToolbarFeature.Action.ViewAction.ShowProblemsLimitInfoModal -> {
                ProblemsLimitInfoBottomSheet.newInstance(
                    params = ProblemsLimitInfoModalFeatureParams(
                        action.subscription,
                        action.chargeLimitsStrategy,
                        action.context
                    )
                ).showIfNotExists(fragmentManager, ProblemsLimitInfoBottomSheet.TAG)
            }
        }
    }

    fun setSubtitle(
        subtitle: String?,
        @DrawableRes drawableEnd: Int? = null
    ) {
        this.subtitle = subtitle
        with(viewBinding.subtitleTextView) {
            isVisible = subtitle != null
            if (subtitle != null) {
                setTextIfChanged(subtitle)
            }
            if (subtitle != null) {
                setCompoundDrawables(end = drawableEnd ?: -1)
            }
        }
        applyInsetsToCollapsingToolbarLayout(
            context = context,
            collapsingToolbarLayout = viewBinding.gamificationCollapsingToolbarLayout,
            subtitle = subtitle
        )
    }

    @Suppress("MagicNumber")
    private fun applyInsetsToCollapsingToolbarLayout(
        context: Context,
        collapsingToolbarLayout: CollapsingToolbarLayout,
        subtitle: String?,
        insetTop: Int = ViewCompat
            .getRootWindowInsets(collapsingToolbarLayout)
            ?.getInsets(WindowInsetsCompat.Type.statusBars())
            ?.top
            ?: 0
    ) {
        val subtitlePaddingVertical =
            if (subtitle != null) {
                context.resources.getDimensionPixelOffset(R.dimen.gamification_toolbar_subtitle_padding_vertical) * 2
            } else {
                0
            }

        collapsingToolbarLayout.expandedTitleMarginBottom =
            if (subtitle != null) {
                context.resources.getDimensionPixelOffset(
                    R.dimen.gamification_toolbar_with_subtitle_expanded_title_margin_bottom
                ) + subtitlePaddingVertical
            } else {
                context.resources.getDimensionPixelOffset(
                    R.dimen.gamification_toolbar_default_expanded_title_margin_bottom
                )
            }

        collapsingToolbarLayout.updateLayoutParams<AppBarLayout.LayoutParams> {
            height = if (subtitle != null) {
                context.resources.getDimensionPixelOffset(R.dimen.gamification_toolbar_with_subtitle_height) +
                    subtitlePaddingVertical
            } else {
                context.resources.getDimensionPixelOffset(R.dimen.gamification_toolbar_default_height)
            } + insetTop
        }
        collapsingToolbarLayout.expandedTitleMarginTop = insetTop + subtitlePaddingVertical
    }
}