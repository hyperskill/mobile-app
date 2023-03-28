package org.hyperskill.app.android.problems_limit.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentProblemsLimitBinding
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProblemsLimitFragment :
    Fragment(R.layout.fragment_problems_limit),
    ReduxView<ProblemsLimitFeature.ViewState, ProblemsLimitFeature.Action.ViewAction> {

    companion object {

        const val TAG = "ProblemsLimitFragment"

        fun newInstance(isDividerVisible: Boolean = false): ProblemsLimitFragment =
            ProblemsLimitFragment().apply {
                this.isDividerVisible = isDividerVisible
            }
    }

    private val viewBinding: FragmentProblemsLimitBinding by viewBinding(FragmentProblemsLimitBinding::bind)

    private lateinit var viewModelFactory: ReduxViewModelFactory
    private val problemsLimitViewModel: ProblemsLimitViewModel by reduxViewModel(this) { viewModelFactory }

    private var viewStateDelegate: ViewStateDelegate<ProblemsLimitFeature.ViewState>? = null

    private var isDividerVisible: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val problemsLimitComponent = HyperskillApp.graph().buildPlatformProblemsLimitComponent()
        viewModelFactory = problemsLimitComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding.problemsLimitDivider.root.isVisible = isDividerVisible
        viewBinding.problemsLimitsContent.updateLayoutParams<MarginLayoutParams> {
            topMargin = if (isDividerVisible) {
                resources.getDimensionPixelOffset(R.dimen.problems_limit_diver_margin)
            } else {
                0
            }
        }
        viewStateDelegate = ViewStateDelegate<ProblemsLimitFeature.ViewState>().apply {
            addState<ProblemsLimitFeature.ViewState.Idle>()
            addState<ProblemsLimitFeature.ViewState.Content.Empty>()

            // Is used to hide fragment container if content is not showed
            val parentView =  viewBinding.root.parent as View

            addState<ProblemsLimitFeature.ViewState.Loading>(
                parentView,
                viewBinding.problemsLimitSkeleton
            )
            if (isDividerVisible) {
                addState<ProblemsLimitFeature.ViewState.Content.Widget>(
                    parentView,
                    viewBinding.problemsLimitDivider.root,
                    viewBinding.problemsLimitsContent
                )
            } else {
                addState<ProblemsLimitFeature.ViewState.Content.Widget>(
                    parentView,
                    viewBinding.problemsLimitsContent
                )
            }
        }
    }

    override fun onDestroyView() {
        viewStateDelegate = null
        super.onDestroyView()
    }

    override fun render(state: ProblemsLimitFeature.ViewState) {
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

    override fun onAction(action: ProblemsLimitFeature.Action.ViewAction) {
        when (action) {
            else -> {
                // no op
            }
        }
    }
}