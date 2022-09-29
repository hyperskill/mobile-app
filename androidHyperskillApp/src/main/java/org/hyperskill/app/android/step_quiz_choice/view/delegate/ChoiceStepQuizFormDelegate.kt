package org.hyperskill.app.android.step_quiz_choice.view.delegate

import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizChoiceBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz_choice.view.adapter.ChoiceMultipleSelectionAdapterDelegate
import org.hyperskill.app.android.step_quiz_choice.view.adapter.ChoiceSingleSelectionAdapterDelegate
import org.hyperskill.app.android.step_quiz_choice.view.mapper.ChoiceStepQuizOptionsMapper
import org.hyperskill.app.android.step_quiz_choice.view.model.Choice
import org.hyperskill.app.step_quiz.domain.model.submissions.ChoiceAnswer
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.app.core.model.safeCast

class ChoiceStepQuizFormDelegate(
    containerBinding: FragmentStepQuizBinding,
    binding: LayoutStepQuizChoiceBinding,
    private val onQuizChanged: (Reply) -> Unit
) : StepQuizFormDelegate {

    private val quizDescription = containerBinding.stepQuizDescription
    private val choiceStepQuizOptionsMapper = ChoiceStepQuizOptionsMapper()
    private var choicesAdapter: DefaultDelegateAdapter<Choice> = DefaultDelegateAdapter()

    init {
        binding.stepQuizChoiceRecyclerView.apply {
            itemAnimator = null
            adapter = choicesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
        }
    }

    override fun setState(state: StepQuizFeature.State.AttemptLoaded) {
        val dataset = state.attempt.dataset ?: return

        @StringRes
        val descriptionRes =
            if (dataset.isMultipleChoice) {
                R.string.step_quiz_choice_multiple_choice_title
            } else {
                R.string.step_quiz_choice_single_choice_title
            }
        quizDescription.setText(descriptionRes)

        val submission = (state.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission

        val reply = submission?.reply

        if (choicesAdapter.delegates.isEmpty()) {
            val delegate =
                if (dataset.isMultipleChoice) {
                    ChoiceMultipleSelectionAdapterDelegate(onClick = ::handleMultipleChoiceClick)
                } else {
                    ChoiceSingleSelectionAdapterDelegate(onClick = ::handleSingleChoiceClick)
                }
            choicesAdapter += delegate
        }

        choicesAdapter.items = choiceStepQuizOptionsMapper.mapChoices(
            dataset.options ?: emptyList(),
            reply?.choices.safeCast<List<ChoiceAnswer.Choice>>()?.map { it.boolValue },
            StepQuizResolver.isQuizEnabled(state)
        )
    }

    override fun createReply(): Reply =
        Reply(choices = choicesAdapter.items.map { ChoiceAnswer.Choice(it.isSelected) })

    private fun handleSingleChoiceClick(choice: Choice) {
        choicesAdapter.items = choicesAdapter.items.map {
            if (it.option == choice.option) {
                it.copy(isSelected = !choice.isSelected)
            } else {
                it.copy(isSelected = false)
            }
        }
        onQuizChanged(createReply())
    }

    private fun handleMultipleChoiceClick(choice: Choice) {
        choicesAdapter.items = choicesAdapter.items.map {
            if (it.option == choice.option) {
                it.copy(isSelected = !choice.isSelected)
            } else {
                it
            }
        }
        onQuizChanged(createReply())
    }
}