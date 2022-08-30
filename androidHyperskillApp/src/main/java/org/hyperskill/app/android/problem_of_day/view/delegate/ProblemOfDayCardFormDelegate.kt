package org.hyperskill.app.android.problem_of_day.view.delegate

import android.content.Context
import android.view.View
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutProblemOfTheDayCardBinding
import org.hyperskill.app.home.presentation.HomeFeature

class ProblemOfDayCardFormDelegate(
    private val context: Context,
    private val binding: LayoutProblemOfTheDayCardBinding,
    private val state: HomeFeature.ProblemOfDayState,
    private val onActionButtonClick: (Long) -> Unit
) {
    companion object {
        const val EMPTY_CARD_STATE_ALPHA = 0.49F
    }

    init {
        setCardState(state)
    }

    private fun updateNextProblemTime(seconds: Long) {
        val hours = (seconds / 3600).toInt()
        val minutes = ((seconds % 3600) / 60).toInt()
        binding.problemOfDayNextProblemInTextView.text =
            buildSpannedString {
                append(
                    "${context.resources.getString(R.string.problem_of_day_next_problem_in)} "
                )
                bold {
                    append(
                        context.resources.getString(
                            R.string.problem_of_day_hours_and_minutes,
                            hours,
                            minutes
                        )
                    )
                }
            }
    }

    private fun setCardState(cardState: HomeFeature.ProblemOfDayState) {
        with(binding) {
            when (cardState) {
                is HomeFeature.ProblemOfDayState.Empty -> {
                    root.isClickable = false

                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInLinearLayout.visibility = View.GONE

                    problemOfDayLayout.alpha = EMPTY_CARD_STATE_ALPHA
                    problemOfDayHexogens.alpha = EMPTY_CARD_STATE_ALPHA

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_no_problems_to_solve)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_blue)
                    problemOfDayActionButton.setIconTintResource(R.color.color_surface)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_task_day, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_uncompleted)
                }
                is HomeFeature.ProblemOfDayState.Solved -> {
                    state as HomeFeature.ProblemOfDayState.Solved

                    root.isClickable = true
                    root.setOnClickListener {
                        onActionButtonClick(cardState.step.id)
                    }

                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInLinearLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_get_back)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_yellow_green)
                    problemOfDayActionButton.setIconTintResource(R.color.color_on_surface_alpha_60)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static_solved)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checkmark, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_completed)

                    updateNextProblemTime(state.nextProblemIn)
                }
                is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                    state as HomeFeature.ProblemOfDayState.NeedToSolve

                    root.isClickable = true
                    root.setOnClickListener {
                        onActionButtonClick(cardState.step.id)
                    }

                    if (state.step.secondsToComplete != null) {
                        problemOfDayTimeToSolveTextView.visibility = View.VISIBLE
                        val minutesToComplete = state.step.secondsToComplete!!.div(60).toInt()
                        problemOfDayTimeToSolveTextView.text =
                            context.resources.getQuantityString(
                                R.plurals.minutes,
                                minutesToComplete,
                                minutesToComplete
                            )
                    } else {
                        problemOfDayTimeToSolveTextView.visibility = View.GONE
                    }

                    problemOfDayNextProblemInLinearLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_solve_a_random_problem)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_blue)
                    problemOfDayActionButton.setIconTintResource(R.color.color_surface)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_task_day, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_uncompleted)

                    updateNextProblemTime(state.nextProblemIn)
                }
            }
        }
    }
}