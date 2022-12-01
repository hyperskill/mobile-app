package org.hyperskill.app.android.topics_repetitions.view.delegate

import android.content.Context
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutTopicsRepetitionHeaderBinding
import org.hyperskill.app.topics_repetitions.view.model.RepetitionsStatus

object TopicsRepetitionHeaderDelegate {
    fun render(
        context: Context,
        binding: LayoutTopicsRepetitionHeaderBinding,
        state: RepetitionsStatus
    ) {
        with(binding) {
            with(topicRepetitionsTitle) {
                setText(
                    when (state) {
                        is RepetitionsStatus.AllTopicsRepeated ->
                            R.string.topics_repetitions_all_topics_repeated_text
                        is RepetitionsStatus.RecommendedTopicsAvailable ->
                            R.string.topics_repetitions_good_job_text
                        is RepetitionsStatus.RecommendedTopicsRepeated ->
                            R.string.topics_repetitions_try_to_recall_text
                    }
                )
                setTextAppearance(
                    when (state) {
                        is RepetitionsStatus.AllTopicsRepeated ->
                            R.style.TextAppearance_AppCompat_Body2
                        is RepetitionsStatus.RecommendedTopicsAvailable,
                        is RepetitionsStatus.RecommendedTopicsRepeated ->
                            R.style.TextAppearance_AppCompat_Body1
                    }
                )
            }

            topicRepetitionsCountLinearLayout.isVisible =
                state is RepetitionsStatus.RecommendedTopicsAvailable
            if (state is RepetitionsStatus.RecommendedTopicsAvailable) {
                topicsRepetitionCountTextView.text =
                    state.recommendedRepetitionsCount.toString()
                topicsRepetitionCountDescriptionTextView.text =
                    context.resources.getQuantityText(
                        R.plurals.topics_to_repeat_today,
                        state.recommendedRepetitionsCount
                    )
            }

            val buttonText =
                (state as? RepetitionsStatus.RecommendedTopicsAvailable)?.repeatButtonText
            topicsRepetitionRepeatButton.isVisible = buttonText != null
            if (buttonText != null) {
                topicsRepetitionRepeatButton.text = buttonText
            }
        }

    }
}