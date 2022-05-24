package org.hyperskill.app.android.step.view.ui.fragment

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentStepQuizBinding

class StepQuizFragment : Fragment(R.layout.fragment_step_quiz) {
    companion object {
        fun newInstance(): StepQuizFragment =
            StepQuizFragment()
    }

    private val viewBinding: FragmentStepQuizBinding by viewBinding(FragmentStepQuizBinding::bind)
}