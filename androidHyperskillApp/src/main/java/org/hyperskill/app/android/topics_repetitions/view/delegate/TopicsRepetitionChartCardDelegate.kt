package org.hyperskill.app.android.topics_repetitions.view.delegate

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import org.hyperskill.app.android.databinding.LayoutTopicsRepetitionChartBinding
import org.hyperskill.app.android.topics_repetitions.view.model.TopicsRepetitionChartState
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

object TopicsRepetitionChartCardDelegate {

    fun render(
        binding: LayoutTopicsRepetitionChartBinding,
        previousState: TopicsRepetitionChartState?,
        state: TopicsRepetitionChartState
    ) {
        with(binding) {
            if (state.chartData == previousState?.chartData) return

            topicsRepetitionChartDescriptionTextView.setTextIfChanged(state.chartDescription)

            val maxValue = state.chartData.maxOfOrNull { it.second } ?: return

            val maxChartValue = maxValue + (10 - maxValue % 10)
            val averageChartValue = maxChartValue / 2

            topicsRepetitionChart1LineNumberTextView.setTextIfChanged(0.toString())
            topicsRepetitionChart3LineNumberTextView.setTextIfChanged(maxChartValue.toString())
            topicsRepetitionChart2LineNumberTextView.setTextIfChanged(averageChartValue.toString())

            state.chartData.forEachIndexed { index, (title, _) ->
                when (index) {
                    0 -> topicsRepetition1TimeTextView.setTextIfChanged(title)
                    1 -> topicsRepetition2TimesTextView.setTextIfChanged(title)
                    2 -> topicsRepetition3TimesTextView.setTextIfChanged(title)
                }
            }

            root.doOnLayout {
                val maxWidth = topicsRepetitionChart3Line.x - topicsRepetitionChart1Line.x
                state.chartData.forEachIndexed { index, (_, value) ->
                    when (index) {
                        0 -> {
                            setupRowTextView(
                                maxWidth,
                                topicsRepetition1RowTextView,
                                value,
                                maxChartValue
                            )
                            topicsRepetition1RowTextView.setTextIfChanged(value.toString())
                        }
                        1 -> {
                            setupRowTextView(
                                maxWidth,
                                topicsRepetition2RowTextView,
                                value,
                                maxChartValue
                            )
                            topicsRepetition2RowTextView.setTextIfChanged(value.toString())
                        }
                        2 -> {
                            setupRowTextView(
                                maxWidth,
                                topicsRepetition3RowTextView,
                                value,
                                maxChartValue
                            )
                            topicsRepetition3RowTextView.setTextIfChanged(value.toString())
                        }
                    }
                }
            }
        }
    }

    private fun setupRowTextView(
        maxWidth: Float,
        textView: TextView,
        value: Int,
        maxChartValue: Int
    ) {
        textView.doOnLayout {
            it.updateLayoutParams<ConstraintLayout.LayoutParams> {
                val percentage = value.toFloat() / maxChartValue
                width = when {
                    percentage == 1f -> {
                        ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
                    }
                    percentage > 0f -> {
                        (maxWidth * percentage).toInt()
                    }
                    else -> {
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    }
                }
            }
        }
    }
}