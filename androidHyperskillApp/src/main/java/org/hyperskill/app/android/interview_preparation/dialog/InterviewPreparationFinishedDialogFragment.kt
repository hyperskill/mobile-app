package org.hyperskill.app.android.interview_preparation.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentInterviewPreparationFinishedBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme

class InterviewPreparationFinishedDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "InterviewPreparationFinishedBottomSheetTag"

        fun newInstance(): InterviewPreparationFinishedDialogFragment =
            InterviewPreparationFinishedDialogFragment()
    }

    private val binding: FragmentInterviewPreparationFinishedBinding by viewBinding(
        FragmentInterviewPreparationFinishedBinding::bind
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
                    (parentFragment as? Callback)?.onInterviewPreparationFinishedDialogShown()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.wrapWithTheme(requireActivity())
            .inflate(R.layout.fragment_interview_preparation_finished, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.interviewFinishedGoTrainingButton.setOnClickListener {
            (parentFragment as? Callback)
                ?.onInterviewPreparationFinishedDialogGoTrainingClicked()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        (parentFragment as? Callback)?.onInterviewPreparationFinishedDialogHidden()
    }

    interface Callback {
        fun onInterviewPreparationFinishedDialogShown()

        fun onInterviewPreparationFinishedDialogHidden()

        fun onInterviewPreparationFinishedDialogGoTrainingClicked()
    }
}