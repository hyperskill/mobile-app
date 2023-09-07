package org.hyperskill.app.android.step_quiz_parsons.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme

class ParsonsStepQuizOnboardingBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ParsonsStepQuizOnboardingBottomSheetDialogFragment"

        fun newInstance(): ParsonsStepQuizOnboardingBottomSheetDialogFragment =
            ParsonsStepQuizOnboardingBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    (parentFragment as? Callback)?.parsonsProblemOnboardingShown()
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
                R.layout.fragment_step_quiz_parsons_onboarding,
                container,
                false
            )

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (parentFragment as? Callback)?.parsonsProblemOnboardingHidden()
    }

    interface Callback {
        fun parsonsProblemOnboardingShown()
        fun parsonsProblemOnboardingHidden()
    }
}