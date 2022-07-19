package org.hyperskill.app.android.problem_of_day.view.delegate

import android.content.Context
import android.view.View
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

    private fun setCardState(cardState: HomeFeature.ProblemOfDayState) {
        with(binding) {
            when (cardState) {
                is HomeFeature.ProblemOfDayState.Empty -> {
                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInLinearLayout.visibility = View.GONE

                    problemOfDayLayout.alpha = EMPTY_CARD_STATE_ALPHA
                    problemOfDayHexogens.alpha = EMPTY_CARD_STATE_ALPHA

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_no_problems_to_solve)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_blue)
                    problemOfDayActionButton.setIconTintResource(R.color.color_surface)
                    problemOfDayActionButton.isClickable = false

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_task_day, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_uncompleted)
                }
                is HomeFeature.ProblemOfDayState.Solved -> {
                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInLinearLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_get_back)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_yellow_green)
                    problemOfDayActionButton.setIconTintResource(R.color.color_on_surface_alpha_60)
                    problemOfDayActionButton.isClickable = true
                    problemOfDayActionButton.setOnClickListener {
                        onActionButtonClick(cardState.step.id)
                    }

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static_solved)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_checkmark, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_completed)

                    // TODO add time
                    problemOfDayNextProblemInTextView.setTextColor(R.color.color_on_surface_alpha_87)
                }
                is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                    problemOfDayTimeToSolveTextView.visibility = View.VISIBLE
                    problemOfDayNextProblemInLinearLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(R.string.problem_of_day_solve_a_random_problem)

                    problemOfDayActionButton.setBackgroundResource(R.drawable.bg_gradient_blue)
                    problemOfDayActionButton.setIconTintResource(R.color.color_surface)
                    problemOfDayActionButton.isClickable = true
                    problemOfDayActionButton.setOnClickListener {
                        onActionButtonClick(cardState.step.id)
                    }

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_task_day, 0, 0, 0)
                    problemOfDayTitleTextView.setText(R.string.problem_of_day_title_uncompleted)

                    // TODO add time
                    problemOfDayNextProblemInTextView.setTextColor(R.color.color_on_surface_alpha_38)
                }
            }
        }
    }
}