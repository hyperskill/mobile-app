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
import org.hyperskill.app.android.databinding.FragmentProblemsLimitReachedBinding
import org.hyperskill.app.android.view.base.ui.extension.wrapWithTheme
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import ru.nobird.android.view.base.ui.extension.argument

class ProblemsLimitReachedBottomSheet : BottomSheetDialogFragment() {

    companion object {

        const val TAG = "ProblemsLimitReachedBottomSheet"

        fun newInstance(
            modalText: String,
            isUnlockUnlimitedProblemsButtonVisible: Boolean
        ): ProblemsLimitReachedBottomSheet =
            ProblemsLimitReachedBottomSheet().apply {
                this.modalText = modalText
                this.isUnlockUnlimitedProblemsButtonVisible = isUnlockUnlimitedProblemsButtonVisible
            }
    }

    private var modalText: String by argument()
    private var isUnlockUnlimitedProblemsButtonVisible: Boolean by argument()

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
            problemsLimitReachedUnlimitedProblemsButton.isVisible = isUnlockUnlimitedProblemsButtonVisible
            if (isUnlockUnlimitedProblemsButtonVisible) {
                problemsLimitReachedUnlimitedProblemsButton.setOnClickListener {
                    viewModel.onNewMessage(
                        StepQuizFeature.Message.ProblemsLimitReachedModalUnlockUnlimitedProblemsClicked
                    )
                }
            }
            problemsLimitReachedDescription.text = modalText
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.onNewMessage(StepQuizFeature.Message.ProblemsLimitReachedModalHiddenEventMessage)
    }
}