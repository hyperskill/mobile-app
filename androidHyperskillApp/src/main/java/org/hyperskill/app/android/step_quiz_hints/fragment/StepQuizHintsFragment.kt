package org.hyperskill.app.android.step_quiz_hints.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.ImageLoader
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizHintsBinding
import org.hyperskill.app.android.step_quiz_hints.delegate.StepQuizHintsDelegate
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsViewModel
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StepQuizHintsFragment :
    Fragment(R.layout.layout_step_quiz_hints),
    ReduxView<StepQuizHintsFeature.ViewState, StepQuizHintsFeature.Action.ViewAction> {

    companion object {
        private const val KEY_STEP = "hints_key_step"
        private const val KEY_STEP_ROUTE = "hints_key_step_route"

        fun newInstance(stepRoute: StepRoute, step: Step): Fragment =
            StepQuizHintsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_STEP, step, serializer = Step.serializer())
                    putParcelable(KEY_STEP_ROUTE, stepRoute, serializer = StepRoute.serializer())
                }
            }
    }

    private var step: Step? = null

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val stepQuizHintsViewModel: StepQuizHintsViewModel by reduxViewModel(this) { requireNotNull(viewModelFactory) }

    private val viewBinding: LayoutStepQuizHintsBinding by viewBinding(LayoutStepQuizHintsBinding::bind)

    private var stepQuizHintsDelegate: StepQuizHintsDelegate? = null

    private val svgImageLoader: ImageLoader by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().imageLoadingComponent.imageLoader
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val step = requireArguments().getParcelable(KEY_STEP, deserializer = Step.serializer()) ?: throw IllegalStateException("Step cannot be null")
        this.step = step
        val stepRoute = requireArguments().getParcelable(KEY_STEP_ROUTE, deserializer = StepRoute.serializer())
            ?: throw IllegalStateException("StepRoute cannot be null")
        injectDependencies(stepRoute, step)
    }

    private fun injectDependencies(stepRoute: StepRoute, step: Step) {
        viewModelFactory =
            HyperskillApp.graph()
                .buildPlatformStepQuizHintsComponent(stepRoute, step)
                .reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        step?.let {
            stepQuizHintsDelegate = StepQuizHintsDelegate(
                binding = viewBinding,
                imageLoader = svgImageLoader,
                onNewMessage = stepQuizHintsViewModel::onNewMessage
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stepQuizHintsDelegate = null
    }

    override fun onAction(action: StepQuizHintsFeature.Action.ViewAction) {
        when (action) {
            StepQuizHintsFeature.Action.ViewAction.ShowNetworkError ->
                view?.snackbar(messageRes = org.hyperskill.app.R.string.connection_error)
        }
    }

    override fun render(state: StepQuizHintsFeature.ViewState) {
        stepQuizHintsDelegate?.render(requireContext(), state)
    }
}