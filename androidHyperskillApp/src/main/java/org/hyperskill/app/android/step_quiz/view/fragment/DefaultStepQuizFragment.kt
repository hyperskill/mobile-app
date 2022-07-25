package org.hyperskill.app.android.step_quiz.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.model.ReplyResult
import org.hyperskill.app.android.step_quiz.view.mapper.StepQuizFeedbackMapper
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFeedbackBlocksDelegate
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizViewStateDelegateFactory
import org.hyperskill.app.android.step_quiz.view.model.StepQuizFeedbackState
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.app.presentation.redux.container.ReduxView

abstract class DefaultStepQuizFragment : Fragment(R.layout.fragment_step_quiz), ReduxView<StepQuizFeature.State, StepQuizFeature.Action.ViewAction> {
    companion object {
        const val KEY_STEP = "key_step"
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
    private val stepQuizViewModel: StepQuizViewModel by viewModels { viewModelFactory }

    private lateinit var viewStateDelegate: ViewStateDelegate<StepQuizFeature.State>
    private lateinit var stepQuizFeedbackBlocksDelegate: StepQuizFeedbackBlocksDelegate
    private lateinit var stepQuizFormDelegate: StepQuizFormDelegate
    private val stepQuizFeedbackMapper = StepQuizFeedbackMapper()

    protected abstract val quizViews: Array<View>
    protected abstract val skeletonView: View

    protected lateinit var step: Step

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        step = requireArguments().getParcelable<Step>(KEY_STEP) ?: throw IllegalStateException()
    }

    private fun injectComponent() {
        val stepQuizComponent = HyperskillApp.graph().buildStepQuizComponent()
        val platformStepQuizComponent = HyperskillApp.graph().buildPlatformStepQuizComponent(stepQuizComponent)
        viewModelFactory = platformStepQuizComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewStateDelegate = StepQuizViewStateDelegateFactory.create(viewBinding, skeletonView, *quizViews)
        stepQuizFeedbackBlocksDelegate = StepQuizFeedbackBlocksDelegate(requireContext(), viewBinding.stepQuizFeedbackBlocks)
        stepQuizFormDelegate = createStepQuizFormDelegate(viewBinding)

        viewBinding.stepQuizSubmitButton.setOnClickListener {
            onActionButtonClicked()
        }
        viewBinding.stepQuizNetworkError.tryAgain.setOnClickListener {
            stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step, forceUpdate = true))
        }

        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.InitWithStep(step))
    }

    protected abstract fun createStepQuizFormDelegate(containerBinding: FragmentStepQuizBinding): StepQuizFormDelegate

    private fun onActionButtonClicked() {
        val replyResult = stepQuizFormDelegate.createReply()
        when (replyResult.validation) {
            is ReplyResult.Validation.Success ->
                stepQuizViewModel.onNewMessage(StepQuizFeature.Message.CreateSubmissionClicked(step, replyResult.reply))

            is ReplyResult.Validation.Error ->
                stepQuizFeedbackBlocksDelegate.setState(StepQuizFeedbackState.Validation(replyResult.validation.message))
        }
    }

    override fun onStart() {
        super.onStart()
        stepQuizViewModel.attachView(this)
    }

    override fun onStop() {
        stepQuizViewModel.detachView(this)
        super.onStop()
    }

    override fun onAction(action: StepQuizFeature.Action.ViewAction) {
        if (action is StepQuizFeature.Action.ViewAction.ShowNetworkError) {
            view?.snackbar(messageRes = R.string.connection_error)
        }
    }

    override fun render(state: StepQuizFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is StepQuizFeature.State.AttemptLoaded) {
            stepQuizFormDelegate.setState(state)
            stepQuizFeedbackBlocksDelegate.setState(stepQuizFeedbackMapper.mapToStepQuizFeedbackState(step.block.name, state))
            viewBinding.stepQuizSubmitButton.isEnabled = StepQuizResolver.isQuizEnabled(state)
        }
    }

    protected fun syncReplyState(replyResult: ReplyResult) {
        stepQuizViewModel.onNewMessage(StepQuizFeature.Message.SyncReply(replyResult.reply))
    }
}