package org.hyperskill.app.android.topics_repetitions.view.delegate

import android.content.Context
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutTopicsRepetitionCardBinding

class TopicsRepetitionCardFormDelegate {
    fun render(
        context: Context,
        binding: LayoutTopicsRepetitionCardBinding,
        recommendedRepetitionsCount: Int,
        isFreemiumEnabled: Boolean
    ) {
        with(binding) {
            topicsRepetitionBackgroundImageView.setImageResource(
                if (recommendedRepetitionsCount > 0) {
                    R.drawable.bg_hexogens_static
                } else {
                    R.drawable.bg_hexogens_static_solved
                }
            )
            topicsRepetitionArrowImageView.setImageResource(
                if (recommendedRepetitionsCount > 0) {
                    R.drawable.ic_home_screen_arrow_button
                } else {
                    R.drawable.ic_home_screen_success_arrow_button
                }
            )
            with(topicsRepetitionCountTextView) {
                isVisible = recommendedRepetitionsCount > 0
                if (recommendedRepetitionsCount > 0) {
                    text = recommendedRepetitionsCount.toString()
                }
            }
            topicsRepetitionCountDescriptionTextView.text = if (recommendedRepetitionsCount > 0) {
                context.resources.getQuantityString(
                    org.hyperskill.app.R.plurals.topics_to_repeat_today,
                    recommendedRepetitionsCount
                )
            } else {
                context.getString(org.hyperskill.app.R.string.topics_repetitions_card_text_completed)
            }
            topicsRepetitionTitleTextView.text = context.getString(
                if (recommendedRepetitionsCount > 0) {
                    org.hyperskill.app.R.string.topics_repetitions_card_title_uncompleted
                } else {
                    org.hyperskill.app.R.string.good_job
                }
            )
            topicsRepetitionFreemiumBadge.isVisible = isFreemiumEnabled && recommendedRepetitionsCount > 0
        }
    }
}