package org.hyperskill.app.android.step_quiz.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding
import org.hyperskill.app.android.step_quiz.view.ui.delegate.SortingStepQuizFormDelegate

class StepQuizFragment : Fragment(R.layout.fragment_step_quiz) {
    companion object {
        fun newInstance(): StepQuizFragment =
            StepQuizFragment()
    }

    private val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
    private lateinit var sortingQuizFormDelegate: SortingStepQuizFormDelegate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortingQuizFormDelegate = SortingStepQuizFormDelegate(viewBinding) {}
    }
}
