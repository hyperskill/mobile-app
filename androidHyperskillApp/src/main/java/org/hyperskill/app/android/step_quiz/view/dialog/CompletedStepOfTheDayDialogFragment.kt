package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.Serializable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.databinding.FragmentCompletedDailyStepBinding
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.presentation.StepViewModel
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature

class CompletedStepOfTheDayDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CompletedStepOfTheDayDialogFragment"

        fun newInstance(
            earnedGemsText: String?,
            shareStreakData: StepCompletionFeature.ShareStreakData
        ): CompletedStepOfTheDayDialogFragment =
            CompletedStepOfTheDayDialogFragment().apply {
                this.params = Params(
                    earnedGemsText = earnedGemsText,
                    shareStreakData = shareStreakData
                )
            }
    }

    // View model should be created in parent fragment
    private val stepViewModel: StepViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val viewBinding: FragmentCompletedDailyStepBinding by viewBinding(FragmentCompletedDailyStepBinding::bind)

    private var params: Params by argument(Params.serializer())

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
            completedDailyStepEarnedGemsTextView.isVisible = params.earnedGemsText != null
            completedDailyStepEarnedGemsTextView.text = params.earnedGemsText

            completedDailyStepStreakTextView.isVisible =
                params.shareStreakData is StepCompletionFeature.ShareStreakData.Content
            completedDailyStepStreakTextView.text =
                (params.shareStreakData as? StepCompletionFeature.ShareStreakData.Content)?.streakText

            completedDailyStepGoBackButton.setOnClickListener {
                stepViewModel.onNewMessage(
                    StepFeature.Message.StepCompletionMessage(
                        StepCompletionFeature.Message.ProblemOfDaySolvedModalGoBackClicked
                    )
                )
                dismiss()
            }

            completedDailyStepShareStreakButton.isVisible =
                params.shareStreakData is StepCompletionFeature.ShareStreakData.Content
            (params.shareStreakData as? StepCompletionFeature.ShareStreakData.Content)?.streak?.let { streak ->
                completedDailyStepShareStreakButton.setOnClickListener {
                    stepViewModel.onNewMessage(
                        StepFeature.Message.StepCompletionMessage(
                            StepCompletionFeature.Message.ProblemOfDaySolvedModalShareStreakClicked(streak)
                        )
                    )
                }
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

    @Serializable
    private data class Params(
        val earnedGemsText: String?,
        val shareStreakData: StepCompletionFeature.ShareStreakData
    )
}