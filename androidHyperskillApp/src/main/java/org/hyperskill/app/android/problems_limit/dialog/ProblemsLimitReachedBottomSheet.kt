package org.hyperskill.app.android.problems_limit.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentProblemsLimitReachedBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel

class ProblemsLimitReachedBottomSheet : BottomSheetDialogFragment() {

    companion object {

        const val TAG = "ProblemsLimitReachedBottomSheet"

        fun newInstance(
            modalData: StepQuizFeature.ProblemsLimitReachedModalData
        ): ProblemsLimitReachedBottomSheet =
            ProblemsLimitReachedBottomSheet().apply {
                this.modalData = modalData
            }
    }

    private var modalData: StepQuizFeature.ProblemsLimitReachedModalData by argument(
        serializer = StepQuizFeature.ProblemsLimitReachedModalData.serializer()
    )

    private val viewBinding: FragmentProblemsLimitReachedBinding by viewBinding(
        FragmentProblemsLimitReachedBinding::bind
    )

    // View model should be created in parent fragment
    private val viewModel: StepQuizViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    viewModel.onNewMessage(StepQuizFeature.Message.ProblemsLimitReachedModalShownEventMessage)
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
                R.layout.fragment_problems_limit_reached,
                container,
                false
            )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            problemsLimitReachedHomeButton.setOnClickListener {
                viewModel.onNewMessage(StepQuizFeature.Message.ProblemsLimitReachedModalGoToHomeScreenClicked)
            }

            problemsLimitReachedModalTitle.text = modalData.title
            problemsLimitReachedDescription.text = modalData.description

            if (modalData.unlockLimitsButtonText != null) {
                problemsLimitReachedUnlimitedProblemsButton.isVisible = true
                problemsLimitReachedUnlimitedProblemsButton.text = modalData.unlockLimitsButtonText
                problemsLimitReachedUnlimitedProblemsButton.setOnClickListener {
                    viewModel.onNewMessage(
                        StepQuizFeature.Message.ProblemsLimitReachedModalUnlockUnlimitedProblemsClicked
                    )
                }
            } else {
                problemsLimitReachedUnlimitedProblemsButton.isVisible = false
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onNewMessage(StepQuizFeature.Message.ProblemsLimitReachedModalHiddenEventMessage)
    }
}