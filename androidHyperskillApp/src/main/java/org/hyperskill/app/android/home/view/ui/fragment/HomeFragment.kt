package org.hyperskill.app.android.home.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentHomeBinding
import org.hyperskill.app.android.databinding.LayoutProblemOfTheDayCardBinding
import org.hyperskill.app.android.problem_of_day.view.delegate.ProblemOfDayCardFormDelegate
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.home.presentation.HomeFeature
import ru.nobird.android.view.base.ui.extension.snackbar

class HomeFragment : Fragment(R.layout.layout_problem_of_the_day_card) {
    companion object {
        fun newInstance(): Fragment =
            HomeFragment()
    }

//    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewBinding: LayoutProblemOfTheDayCardBinding by viewBinding(LayoutProblemOfTheDayCardBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ProblemOfDayCardFormDelegate(
            requireContext(),
            viewBinding,
            HomeFeature.ProblemOfDayState.Empty,
            ::onActionButtonClicked
        )

//        viewBinding.homeAction.setOnClickListener {
//            val stepId = viewBinding.homeInputEditText.text.toString().toLongOrNull()
//            if (stepId == null) {
//                view.snackbar("Insert a valid number", Snackbar.LENGTH_SHORT)
//            } else {
//                requireRouter().navigateTo(StepScreen(stepId))
//            }
//        }
    }

    private fun onActionButtonClicked(stepId: Long) {
        requireRouter().navigateTo(StepScreen(stepId))
    }
}