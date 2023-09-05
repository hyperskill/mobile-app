package org.hyperskill.app.android.problems_limit.view.ui.delegate

import androidx.core.view.isVisible
import kotlin.math.roundToInt
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
                with(viewBinding) {
                    problemsLimitIndicator.progress = (100 * state.progress).roundToInt()
                    problemsLimitCount.setTextIfChanged(state.stepsLimitLabel)

                    val updateInLabel = state.updateInLabel
                    problemsLimitUpdatedIn.isVisible = updateInLabel != null
                    if (updateInLabel != null) {
                        problemsLimitUpdatedIn.setTextIfChanged(updateInLabel)
                    }
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