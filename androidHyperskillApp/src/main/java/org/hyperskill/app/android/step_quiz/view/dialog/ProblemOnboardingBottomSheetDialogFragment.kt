package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentStepQuizProblemOnboardingBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode

class ProblemOnboardingBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ProblemOnboardingBottomSheetDialogFragment"

        fun newInstance(
            modalType: StepQuizFeature.ProblemOnboardingModal
        ): ProblemOnboardingBottomSheetDialogFragment =
            ProblemOnboardingBottomSheetDialogFragment().apply {
                this.modalType = modalType
            }
    }

    private var modalType: StepQuizFeature.ProblemOnboardingModal by argument(
        StepQuizFeature.ProblemOnboardingModal.serializer()
    )

    private val viewBinding: FragmentStepQuizProblemOnboardingBinding by viewBinding(
        FragmentStepQuizProblemOnboardingBinding::bind
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    (parentFragment as? Callback)?.problemOnboardingShown(modalType)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(
                R.layout.fragment_step_quiz_problem_onboarding,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            parsonsOnboardingAnimation.setAnimation(getAnimation(modalType))
            parsonsOnboardingDescription.text =
                getDescription(requireContext(), modalType)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? Callback)?.problemOnboardingHidden(modalType)
    }

    @RawRes
    private fun getAnimation(modalType: StepQuizFeature.ProblemOnboardingModal): Int =
        when (modalType) {
            StepQuizFeature.ProblemOnboardingModal.Parsons -> R.raw.parsons_problem_onboarding_animation
            is StepQuizFeature.ProblemOnboardingModal.FillBlanks -> when (modalType.mode) {
                FillBlanksMode.INPUT -> R.raw.fill_blanks_input_onboarding_animation
                FillBlanksMode.SELECT -> R.raw.fill_blanks_select_onboarding_animation
            }
        }

    private fun getDescription(
        context: Context,
        modalType: StepQuizFeature.ProblemOnboardingModal
    ): String =
        context.getString(
            when (modalType) {
                StepQuizFeature.ProblemOnboardingModal.Parsons ->
                    org.hyperskill.app.R.string.step_quiz_problem_onboarding_modal_parsons_description
                is StepQuizFeature.ProblemOnboardingModal.FillBlanks ->
                    org.hyperskill.app.R.string.step_quiz_problem_onboarding_modal_fill_blanks_description
            }
        )

    interface Callback {
        fun problemOnboardingShown(modalType: StepQuizFeature.ProblemOnboardingModal)
        fun problemOnboardingHidden(modalType: StepQuizFeature.ProblemOnboardingModal)
    }
}