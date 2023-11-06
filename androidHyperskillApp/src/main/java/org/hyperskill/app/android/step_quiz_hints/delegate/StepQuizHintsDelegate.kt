package org.hyperskill.app.android.step_quiz_hints.delegate

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import coil.ImageLoader
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.R
import org.hyperskill.app.android.databinding.LayoutStepQuizHintsBinding
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged
import ru.nobird.android.view.base.ui.extension.snackbar

class StepQuizHintsDelegate(
    private val binding: LayoutStepQuizHintsBinding,
    private val imageLoader: ImageLoader,
    private val onNewMessage: (StepQuizHintsFeature.Message) -> Unit
) {
    init {
        with(binding) {
            with(stepQuizSeeHintsButton.root) {
                setText(R.string.step_quiz_hints_show_button_text)
                setOnClickListener {
                    onNewMessage(StepQuizHintsFeature.Message.LoadHintButtonClicked)
                }
            }

            with(stepQuizHintCard) {
                stepQuizHintContentTextView.movementMethod = LinkMovementMethod.getInstance()
                stepQuizHintContentTextView.setTextIsSelectable(true)

                stepQuizSeeNextHintButton.root.setText(R.string.step_quiz_hints_see_next_hint)
                stepQuizSeeNextHintButton.root.setOnClickListener {
                    onNewMessage(StepQuizHintsFeature.Message.LoadHintButtonClicked)
                }

                stepQuizHintUselessButton.setOnClickListener {
                    onNewMessage(StepQuizHintsFeature.Message.ReactionButtonClicked(ReactionType.UNHELPFUL))
                }
                stepQuizHintHelpfulButton.setOnClickListener {
                    onNewMessage(StepQuizHintsFeature.Message.ReactionButtonClicked(ReactionType.HELPFUL))
                }
                stepQuizHintsRetryButton.setOnClickListener {
                    onNewMessage(StepQuizHintsFeature.Message.LoadHintButtonClicked)
                }
            }
        }
    }

    private val viewStateDelegate = ViewStateDelegate<StepQuizHintsFeature.ViewState>().apply {
        addState<StepQuizHintsFeature.ViewState.Idle>()
        addState<StepQuizHintsFeature.ViewState.InitialLoading>(binding.stepQuizSeeHintsStub)
        addState<StepQuizHintsFeature.ViewState.HintLoading>(binding.stepQuizHintLoadingView)
        addState<StepQuizHintsFeature.ViewState.Content.SeeHintButton>(binding.stepQuizSeeHintsButton.root)
        addState<StepQuizHintsFeature.ViewState.Content.HintCard>(binding.stepQuizHintCard.root)
        addState<StepQuizHintsFeature.ViewState.Error>(binding.stepQuizHintsRetryButton)
    }

    fun onAction(action: StepQuizHintsFeature.Action.ViewAction) {
        when (action) {
            StepQuizHintsFeature.Action.ViewAction.ShowNetworkError ->
                binding.root.snackbar(messageRes = R.string.connection_error)
        }
    }

    fun render(context: Context, state: StepQuizHintsFeature.ViewState) {
        viewStateDelegate.switchState(state)
        binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(
                top = if (state is StepQuizHintsFeature.ViewState.Idle) {
                    0
                } else {
                    context.resources.getDimensionPixelOffset(
                        org.hyperskill.app.android.R.dimen.step_quiz_hints_top_margin
                    )
                }
            )
        }
        if (state is StepQuizHintsFeature.ViewState.Content.HintCard) {
            renderCardState(state, context)
        }
    }

    private fun renderCardState(
        state: StepQuizHintsFeature.ViewState.Content.HintCard,
        context: Context
    ) {
        with(binding.stepQuizHintCard) {
            stepQuizHintNameTextView.setTextIfChanged(state.authorName)
            stepQuizHintAvatarImageView.load(state.authorAvatar, imageLoader) {
                transformations(CircleCropTransformation())
            }
            if (stepQuizHintContentTextView.originalText != state.hintText) {
                stepQuizHintContentTextView.originalText = state.hintText
            }
            stepQuizHintBeforeRateGroup.isVisible =
                state.hintState == StepQuizHintsFeature.ViewState.HintState.REACT_TO_HINT
            stepQuizSeeNextHintButton.root.isVisible =
                state.hintState == StepQuizHintsFeature.ViewState.HintState.SEE_NEXT_HINT
            stepQuizHintDescriptionTextView.isVisible =
                state.hintState != StepQuizHintsFeature.ViewState.HintState.SEE_NEXT_HINT
            if (state.hintState != StepQuizHintsFeature.ViewState.HintState.SEE_NEXT_HINT) {
                @Suppress("KotlinConstantConditions")
                stepQuizHintDescriptionTextView.setTextIfChanged(
                    when (state.hintState) {
                        StepQuizHintsFeature.ViewState.HintState.REACT_TO_HINT ->
                            R.string.step_quiz_hints_helpful_question_text

                        StepQuizHintsFeature.ViewState.HintState.LAST_HINT ->
                            R.string.step_quiz_hints_last_hint_text

                        StepQuizHintsFeature.ViewState.HintState.SEE_NEXT_HINT ->
                            error("Can't evaluate text for state = $state")
                    }.let(context::getString)
                )
            }
            if (state.hintState == StepQuizHintsFeature.ViewState.HintState.REACT_TO_HINT) {
                stepQuizHintReportTextView.setOnClickListener {
                    handleHintReportClick(context, onNewMessage)
                }
            }
        }
    }

    private fun handleHintReportClick(
        context: Context,
        onNewMessage: (StepQuizHintsFeature.Message) -> Unit
    ) {
        onNewMessage(StepQuizHintsFeature.Message.ClickedReportEventMessage)
        buildHintReportAlert(context, onNewMessage).show()
        onNewMessage(StepQuizHintsFeature.Message.ReportHintNoticeShownEventMessage)
    }

    private fun buildHintReportAlert(
        context: Context,
        onNewMessage: (StepQuizHintsFeature.Message) -> Unit
    ) =
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.step_quiz_hints_report_alert_title)
            .setMessage(R.string.step_quiz_hints_report_alert_text)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                onNewMessage(StepQuizHintsFeature.Message.ReportHintNoticeHiddenEventMessage(true))
                onNewMessage(StepQuizHintsFeature.Message.ReportHint)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
                onNewMessage(
                    StepQuizHintsFeature.Message.ReportHintNoticeHiddenEventMessage(false)
                )
            }
}