package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentCompletedDailyStepBinding
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.argument

class CompletedStepOfTheDayDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "CompletedStepOfTheDayDialogFragment"

        fun newInstance(earnedGemsText: String): CompletedStepOfTheDayDialogFragment =
            CompletedStepOfTheDayDialogFragment().apply {
                this.earnedGemsText = earnedGemsText
            }
    }

    // View model should be created in parent fragment
    private val stepViewModel: StepViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val viewBinding: FragmentCompletedDailyStepBinding by viewBinding(FragmentCompletedDailyStepBinding::bind)

    private var earnedGemsText: String by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TopCornersRoundedBottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).also { dialog ->
            dialog.setOnShowListener {
                dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (savedInstanceState == null) {
                    stepViewModel.onNewMessage(
                        StepFeature.Message.StepCompletionMessage(
                            StepCompletionFeature.Message.DailyStepCompletedModalShownEventMessage
                        )
                    )
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hack to apply AppTheme to content
        // Without contextThemeWrapper AppTheme is not applied
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(R.layout.fragment_completed_daily_step, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            completedDailyStepEarnedGemsTextView.text = earnedGemsText
            completedDailyStepGoBackButton.setOnClickListener {
                stepViewModel.onNewMessage(
                    StepFeature.Message.StepCompletionMessage(
                        StepCompletionFeature.Message.ProblemOfDaySolvedModalGoBackClicked
                    )
                )
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        stepViewModel.onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.DailyStepCompletedModalHiddenEventMessage
            )
        )
    }
}