package org.hyperskill.app.android.problems_limit.view.ui.delegate

import org.hyperskill.app.android.databinding.LayoutProblemsLimitBinding
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class ProblemsLimitDelegate(
    private val viewBinding: LayoutProblemsLimitBinding,
    private val onNewMessage: (ProblemsLimitFeature.Message) -> Unit
) {

    private var viewStateDelegate: ViewStateDelegate<ProblemsLimitFeature.ViewState>? = null

    fun setup() {
        viewStateDelegate = ViewStateDelegate<ProblemsLimitFeature.ViewState>().apply {
            addState<ProblemsLimitFeature.ViewState.Idle>()
            addState<ProblemsLimitFeature.ViewState.Content.Empty>()

            addState<ProblemsLimitFeature.ViewState.Loading>(
                viewBinding.problemsLimitSkeleton
            )

            addState<ProblemsLimitFeature.ViewState.Content.Widget>(
                viewBinding.problemsLimitsContent
            )

            addState<ProblemsLimitFeature.ViewState.Error>(
                viewBinding.problemsLimitRetryButton
            )
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
            else -> {
                // no op
            }
        }
    }

    fun cleanup() {
        viewStateDelegate = null
    }
}