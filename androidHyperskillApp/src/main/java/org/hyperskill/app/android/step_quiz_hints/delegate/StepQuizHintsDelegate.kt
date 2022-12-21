package org.hyperskill.app.android.step_quiz_hints.delegate

import android.content.Context
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizHintsBinding
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step_quiz_hints.model.StepQuizHintsViewState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

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

    private val viewStateDelegate = ViewStateDelegate<StepQuizHintsViewState>().apply {
        addState<StepQuizHintsViewState.Idle>()
        addState<StepQuizHintsViewState.InitialLoading>(binding.stepQuizSeeHintsStub)
        addState<StepQuizHintsViewState.HintLoading>(binding.stepQuizHintLoadingView)
        addState<StepQuizHintsViewState.Content.SeeHintButton>(binding.stepQuizSeeHintsButton.root)
        addState<StepQuizHintsViewState.Content.HintCard>(binding.stepQuizHintCard.root)
        addState<StepQuizHintsViewState.Error>(binding.stepQuizHintsRetryButton)
    }

    fun render(context: Context, state: StepQuizHintsViewState) {
        viewStateDelegate.switchState(state)
        if (state is StepQuizHintsViewState.Content.HintCard) {
            with(binding.stepQuizHintCard) {
                stepQuizHintNameTextView.setTextIfChanged(state.authorName)
                stepQuizHintAvatarImageView.load(state.authorAvatar, imageLoader) {
                    transformations(CircleCropTransformation())
                }
                if (stepQuizHintContentTextView.originalText != state.hintText) {
                    stepQuizHintContentTextView.originalText = state.hintText
                }
                stepQuizHintBeforeRateGroup.isVisible =
                    state.hintState == StepQuizHintsViewState.HintState.ReactToHint
                stepQuizSeeNextHintButton.root.isVisible =
                    state.hintState == StepQuizHintsViewState.HintState.SeeNextHint
                stepQuizHintDescriptionTextView.isVisible =
                    state.hintState != StepQuizHintsViewState.HintState.SeeNextHint
                if (state.hintState != StepQuizHintsViewState.HintState.SeeNextHint) {
                    @Suppress("KotlinConstantConditions")
                    stepQuizHintDescriptionTextView.setTextIfChanged(
                        when (state.hintState) {
                            StepQuizHintsViewState.HintState.ReactToHint -> R.string.step_quiz_hints_helpful_question_text
                            StepQuizHintsViewState.HintState.LastHint -> R.string.step_quiz_hints_last_hint_text
                            StepQuizHintsViewState.HintState.SeeNextHint -> error("Can't evaluate text for state = $state")
                        }.let(context::getString)
                    )
                }
                if (state.hintState == StepQuizHintsViewState.HintState.ReactToHint) {
                    stepQuizHintReportTextView.setOnClickListener {
                        handleHintReportClick(context, onNewMessage)
                    }
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