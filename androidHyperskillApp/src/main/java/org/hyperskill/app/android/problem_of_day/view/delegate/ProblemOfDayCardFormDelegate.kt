package org.hyperskill.app.android.problem_of_day.view.delegate

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutProblemOfTheDayCardBinding
import org.hyperskill.app.home.presentation.HomeFeature

class ProblemOfDayCardFormDelegate(
    private val onCardClicked: (Long) -> Unit,
    private val onReloadClick: () -> Unit
) {
    companion object {
        const val EMPTY_CARD_STATE_ALPHA = 0.49F
    }

    fun setup(binding: LayoutProblemOfTheDayCardBinding) {
        binding.problemOfDayReloadButton.setOnClickListener {
            onReloadClick()
        }
    }

    fun render(
        context: Context,
        binding: LayoutProblemOfTheDayCardBinding,
        state: HomeFeature.ProblemOfDayState,
        isFreemiumEnabled: Boolean
    ) {
        with(binding) {
            when (state) {
                is HomeFeature.ProblemOfDayState.Empty -> {
                    root.isClickable = false

                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInFrameLayout.visibility = View.GONE

                    problemOfDayLayout.alpha = EMPTY_CARD_STATE_ALPHA
                    problemOfDayHexogens.alpha = EMPTY_CARD_STATE_ALPHA

                    problemOfDayDescriptionTextView.setText(org.hyperskill.app.R.string.problem_of_day_no_problems_to_solve)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                        /*left =*/ R.drawable.ic_task_day,
                        0,
                        /*right =*/ R.drawable.ic_home_screen_arrow_button,
                        0
                    )
                    problemOfDayTitleTextView.setText(org.hyperskill.app.R.string.problem_of_day_title_uncompleted)
                }
                is HomeFeature.ProblemOfDayState.Solved -> {
                    root.isClickable = true
                    root.setOnClickListener {
                        onCardClicked(state.step.id)
                    }

                    problemOfDayTimeToSolveTextView.visibility = View.GONE
                    problemOfDayNextProblemInFrameLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(org.hyperskill.app.R.string.problem_of_day_get_back)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static_solved)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                        /*left =*/ R.drawable.ic_checkmark,
                        0,
                        /*right =*/ R.drawable.ic_home_screen_success_arrow_button,
                        0
                    )
                    problemOfDayTitleTextView.setText(org.hyperskill.app.R.string.problem_of_day_title_completed)
                }
                is HomeFeature.ProblemOfDayState.NeedToSolve -> {
                    root.isClickable = true
                    root.setOnClickListener {
                        onCardClicked(state.step.id)
                    }

                    if (state.step.secondsToComplete != null) {
                        problemOfDayTimeToSolveTextView.visibility = View.VISIBLE
                        val minutesToComplete = state.step.secondsToComplete!!.div(60).toInt()
                        problemOfDayTimeToSolveTextView.text =
                            context.getString(
                                org.hyperskill.app.R.string.problem_of_day_minutes,
                                minutesToComplete
                            )
                    } else {
                        problemOfDayTimeToSolveTextView.visibility = View.GONE
                    }

                    problemOfDayNextProblemInFrameLayout.visibility = View.VISIBLE

                    problemOfDayLayout.alpha = 1F
                    problemOfDayHexogens.alpha = 1F

                    problemOfDayDescriptionTextView.setText(org.hyperskill.app.R.string.problem_of_day_solve_a_random_problem)

                    problemOfDayHexogens.setImageResource(R.drawable.bg_hexogens_static)

                    problemOfDayTitleTextView.setCompoundDrawablesWithIntrinsicBounds(
                        /*left =*/ R.drawable.ic_task_day,
                        0,
                        /*right =*/ R.drawable.ic_home_screen_arrow_button,
                        0
                    )
                    problemOfDayTitleTextView.setText(org.hyperskill.app.R.string.problem_of_day_title_uncompleted)
                }
            }
        }
        renderFooter(binding, state, isFreemiumEnabled)
    }

    private fun renderFooter(
        binding: LayoutProblemOfTheDayCardBinding,
        state: HomeFeature.ProblemOfDayState,
        isFreemiumEnabled: Boolean
    ) {
        val needToRefresh = when (state) {
            HomeFeature.ProblemOfDayState.Empty -> false
            is HomeFeature.ProblemOfDayState.NeedToSolve -> state.needToRefresh
            is HomeFeature.ProblemOfDayState.Solved -> state.needToRefresh
        }
        val nextProblemIn = when (state) {
            HomeFeature.ProblemOfDayState.Empty -> null
            is HomeFeature.ProblemOfDayState.NeedToSolve -> state.nextProblemIn
            is HomeFeature.ProblemOfDayState.Solved -> state.nextProblemIn
        }
        with(binding) {
            problemOfDayNextProblemInFrameLayout.isVisible =
                needToRefresh || nextProblemIn != null

            problemOfDayReloadButton.isVisible = needToRefresh

            val isNextProblemInVisible = !needToRefresh && nextProblemIn != null
            nextProblemInLayout.isVisible = isNextProblemInVisible
            if (isNextProblemInVisible) {
                problemOfDayNextProblemInCounterView.text = nextProblemIn
            }
        }
        binding.problemOfDayFreemiumBadge.isVisible = when (state) {
            is HomeFeature.ProblemOfDayState.NeedToSolve -> isFreemiumEnabled
            HomeFeature.ProblemOfDayState.Empty,
            is HomeFeature.ProblemOfDayState.Solved -> false
        }
    }
}