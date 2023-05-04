package org.hyperskill.app.android.problems_limit.view.ui.delegate

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutProblemsLimitBinding
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class ProblemsLimitDelegate(
    private val viewBinding: LayoutProblemsLimitBinding,
    private val onNewMessage: (ProblemsLimitFeature.Message) -> Unit
) {

    private var viewStateDelegate: ViewStateDelegate<ProblemsLimitFeature.ViewState>? = null

    fun setup(context: Context, isDividerVisible: Boolean = false) {
        viewBinding.problemsLimitDivider.root.isVisible = isDividerVisible
        viewBinding.problemsLimitsContent.updateLayoutParams<MarginLayoutParams> {
            topMargin = if (isDividerVisible) {
                context.resources.getDimensionPixelOffset(R.dimen.problems_limit_diver_margin)
            } else {
                0
            }
        }
        viewStateDelegate = ViewStateDelegate<ProblemsLimitFeature.ViewState>().apply {
            addState<ProblemsLimitFeature.ViewState.Idle>()
            addState<ProblemsLimitFeature.ViewState.Content.Empty>()

            addState<ProblemsLimitFeature.ViewState.Loading>(
                viewBinding.problemsLimitSkeleton
            )
            if (isDividerVisible) {
                addState<ProblemsLimitFeature.ViewState.Content.Widget>(
                    viewBinding.problemsLimitDivider.root,
                    viewBinding.problemsLimitsContent
                )
            } else {
                addState<ProblemsLimitFeature.ViewState.Content.Widget>(
                    viewBinding.problemsLimitsContent
                )
            }

            if (isDividerVisible) {
                addState<ProblemsLimitFeature.ViewState.Error>(
                    viewBinding.problemsLimitDivider.root,
                    viewBinding.problemsLimitRetryButton
                )
            } else {
                addState<ProblemsLimitFeature.ViewState.Error>(
                    viewBinding.problemsLimitRetryButton
                )
            }
        }

        viewBinding.problemsLimitRetryButton.setOnClickListener {
            onNewMessage(
                ProblemsLimitFeature.Message.Initialize(forceUpdate = true)
            )
        }
    }

    fun render(state: ProblemsLimitFeature.ViewState) {
        viewStateDelegate?.switchState(state)
        when (state) {
            is ProblemsLimitFeature.ViewState.Content.Widget -> {
                viewBinding.problemsLimitsDots.setData(
                    totalCount = state.stepsLimitTotal,
                    activeCount = state.stepsLimitLeft
                )
                viewBinding.problemsLimitCount.setTextIfChanged(state.stepsLimitLabel)
                viewBinding.problemsLimitUpdatedIn.setTextIfChanged(state.updateInLabel)
            }
            is ProblemsLimitFeature.ViewState.Content.Empty -> {
                // Hack to remove top margin from root widget view if content is not visible
                ((viewBinding.root.parent as? ViewGroup)?.layoutParams as? MarginLayoutParams)?.apply {
                    updateMargins(top = 0)
                }
            }
            else -> {
                // no op
            }
        }
    }

    fun cleanup() {
        viewStateDelegate = null
    }
}