package org.hyperskill.app.android.step_practice.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStepPracticeBinding
import org.hyperskill.app.android.step_content_text.view.fragment.TextStepContentFragment
import org.hyperskill.app.android.step_quiz.view.factory.StepQuizFragmentFactory
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.extension.TimeFancifier
import org.hyperskill.app.step.domain.model.Step

class StepPracticeFragment : Fragment(R.layout.fragment_step_practice) {
    companion object {
        private const val STEP_CONTENT_FRAGMENT_TAG = "step_content"
        private const val STEP_QUIZ_FRAGMENT_TAG = "step_quiz"
        private const val KEY_STEP = "key_step"

        fun newInstance(step: Step): Fragment {
            val arguments = Bundle().apply {
                putParcelable(KEY_STEP, step)
            }
            return StepPracticeFragment().apply {
                this.arguments = arguments
            }
        }
    }
    private lateinit var resourceProvider: ResourceProvider

    private val viewBinding: FragmentStepPracticeBinding by viewBinding(FragmentStepPracticeBinding::bind)

    private lateinit var step: Step

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        step = requireArguments().getParcelable<Step>(KEY_STEP) ?: throw IllegalStateException("Step cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.stepPracticeAppBar.stepQuizToolbar.root.setNavigationOnClickListener {
            requireRouter().exit()
        }
        viewBinding.stepPracticeAppBar.stepQuizToolbar.stepQuizToolbarTitle.text =
            step.title

        viewBinding.stepPracticeCompletion.text = resourceProvider.getString(
            SharedResources.strings.step_quiz_stats_text,
            step.solvedBy.toString(),
            TimeFancifier.formatTimeDistance(step.millisSinceLastCompleted)
        )
        initStepTheoryFragment(step)
        setStepQuizFragment(step)
    }

    private fun injectComponent() {
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
    }

    private fun initStepTheoryFragment(step: Step) {
        if (childFragmentManager.findFragmentByTag(STEP_CONTENT_FRAGMENT_TAG) == null) {
            val stepContentFragment = TextStepContentFragment.newInstance(step)

            childFragmentManager
                .beginTransaction()
                .add(R.id.stepTheoryContainer, stepContentFragment, STEP_CONTENT_FRAGMENT_TAG)
                .commitNow()
        }
    }

    private fun setStepQuizFragment(step: Step) {
        val isQuizFragmentEmpty = childFragmentManager.findFragmentByTag(STEP_QUIZ_FRAGMENT_TAG) == null

        if (isQuizFragmentEmpty) {
            val quizFragment = StepQuizFragmentFactory.getQuizFragment(step)

            childFragmentManager
                .beginTransaction()
                .add(R.id.stepQuizContainer, quizFragment, STEP_QUIZ_FRAGMENT_TAG)
                .commitNow()
        }
    }
}