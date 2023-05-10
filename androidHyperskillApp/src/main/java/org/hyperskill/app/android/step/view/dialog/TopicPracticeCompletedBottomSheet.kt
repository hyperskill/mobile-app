package org.hyperskill.app.android.step.view.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.databinding.FragmentTopicPracticeCompletedBinding
import org.hyperskill.app.android.step.view.model.StepCompletionHost
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.argument

class TopicPracticeCompletedBottomSheet : BottomSheetDialogFragment() {
    companion object {
        const val Tag = "TopicPracticeCompletedBottomSheet"

        fun newInstance(
            title: String,
            isNextStepAvailable: Boolean
        ): TopicPracticeCompletedBottomSheet =
            TopicPracticeCompletedBottomSheet().apply {
                this.title = title
                this.isNextStepAvailable = isNextStepAvailable
            }
    }

    private var title: String by argument()
    private var isNextStepAvailable: Boolean by argument()

    private val viewBinding: FragmentTopicPracticeCompletedBinding by viewBinding(
        FragmentTopicPracticeCompletedBinding::bind
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
                    onNewMessage(StepCompletionFeature.Message.TopicCompletedModalShownEventMessage)
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
            .inflate(
                R.layout.fragment_topic_practice_completed,
                container,
                false
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewBinding) {
            completedDailyStepDescriptionTextView.text = title

            completedDailyStepGoBackButton.isVisible = isNextStepAvailable
            completedDailyStepGoBackButton.setOnClickListener {
                dismiss()
                onNewMessage(StepCompletionFeature.Message.TopicCompletedModalGoToHomeScreenClicked)
            }

            completedDailyStepPrimaryButton.setText(
                if (isNextStepAvailable) {
                    org.hyperskill.app.R.string.step_quiz_topic_completed_continue_with_next_topic_button_text
                } else {
                    org.hyperskill.app.R.string.go_to_homescreen
                }
            )
            completedDailyStepPrimaryButton.setOnClickListener {
                dismiss()
                onNewMessage(
                    if (isNextStepAvailable) {
                        StepCompletionFeature.Message.TopicCompletedModalContinueNextTopicClicked
                    } else {
                        StepCompletionFeature.Message.TopicCompletedModalGoToHomeScreenClicked
                    }
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onNewMessage(StepCompletionFeature.Message.TopicCompletedModalHiddenEventMessage)
    }

    private fun onNewMessage(message: StepCompletionFeature.Message) {
        parentOfType(StepCompletionHost::class.java)?.onNewMessage(message)
    }
}