package org.hyperskill.app.android.stage_implementation.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.fragment.setChildFragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStageImplentationBinding
import org.hyperskill.app.android.step_quiz_code.view.fragment.CodeStepQuizFragment

class StageImplementationFragment : Fragment(R.layout.fragment_stage_implentation) {

    companion object {
        const val TAG = "StageImplementationFragment"
        private const val CODE_QUIZ_TAG = "code_quiz"

        fun newInstance(): StageImplementationFragment =
            StageImplementationFragment()
    }

    private val viewBinding: FragmentStageImplentationBinding by viewBinding(FragmentStageImplentationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding.stageImplementationAppBar) {
            stageImplementationToolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }
            stageImplementationToolbarTitle.text = TODO("Set stage implementation title")
        }
        setupCodeStepQuizFragment()
    }

    private fun setupCodeStepQuizFragment() {
        setChildFragment(R.id.stageQuizContainer, CODE_QUIZ_TAG) {
            CodeStepQuizFragment.newInstance(
                step = TODO(),
                stepRoute = TODO()
            )
        }
    }
}