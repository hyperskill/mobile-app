package org.hyperskill.app.android.step_feedback.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.view.postDelayed
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.hyperskill.app.R
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.requestFocus
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import org.hyperskill.app.step_feedback.presentation.StepFeedbackViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StepFeedbackDialogFragment :
    DialogFragment(),
    ReduxView<StepFeedbackFeature.ViewState, StepFeedbackFeature.Action.ViewAction> {
    companion object {
        const val TAG = "StepFeedbackDialogFragment"
        private const val KEYBOARD_SHOW_DELAY = 100L

        fun newInstance(stepRoute: StepRoute): StepFeedbackDialogFragment =
            StepFeedbackDialogFragment().apply {
                this.stepRoute = stepRoute
            }
    }

    private var stepRoute: StepRoute by argument(StepRoute.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val stepFeedbackViewModel: StepFeedbackViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private var sendButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory = HyperskillApp.graph()
            .buildPlatformStepFeedbackComponent(stepRoute).reduxViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.step_theory_feedback_alert_title)
            .setView(org.hyperskill.app.android.R.layout.view_step_theory_feedback)
            .setPositiveButton(R.string.step_theory_feedback_alert_send_button, null)
            .setNegativeButton(R.string.step_theory_feedback_alert_cancel_button, null)
            .create()
            .also { alertDialog ->
                alertDialog.setOnShowListener {
                    onDialogShown(alertDialog)
                }
            }

    private fun onDialogShown(alertDialog: AlertDialog) {
        val sendButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setOnClickListener {
                onSendClick()
            }
        }
        this.sendButton = sendButton
        setupFeedbackTextView(alertDialog)
        stepFeedbackViewModel.onNewMessage(Message.AlertShown)
    }

    private fun setupFeedbackTextView(alertDialog: AlertDialog) {
        getFeedbackTextView(alertDialog)?.apply {
            doAfterTextChanged {
                onFeedbackTextChanged(it?.toString())
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSendClick()
                }
                false
            }
            postDelayed(KEYBOARD_SHOW_DELAY) {
                requestFocus(requireContext())
            }
        }
    }

    private fun onFeedbackTextChanged(text: String?) {
        stepFeedbackViewModel.onNewMessage(
            Message.FeedbackTextChanged(text)
        )
    }

    private fun onSendClick() {
        stepFeedbackViewModel.onNewMessage(Message.SendButtonClicked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.sendButton = null
    }

    override fun onCancel(dialog: DialogInterface) {
        stepFeedbackViewModel.onNewMessage(Message.AlertHidden)
    }

    override fun onAction(action: StepFeedbackFeature.Action.ViewAction) {
        when (action) {
            StepFeedbackFeature.Action.ViewAction.ShowSendSuccessAndHideModal -> {
                parentFragment?.requireView()?.snackbar(R.string.step_theory_feedback_alert_success_text)
                dismiss()
            }
        }
    }

    override fun render(state: StepFeedbackFeature.ViewState) {
        sendButton?.isEnabled = state.isSendButtonEnabled
    }

    private fun getFeedbackTextView(alertDialog: AlertDialog): TextInputEditText? =
        alertDialog.findViewById(org.hyperskill.app.android.R.id.theoryFeedbackEditText)
}