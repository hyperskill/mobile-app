package org.hyperskill.app.android.theory_feedback.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.R
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TheoryFeedbackDialogFragment :
    DialogFragment(),
    ReduxView<TheoryFeedbackFeature.ViewState, TheoryFeedbackFeature.Action.ViewAction> {
    companion object {
        const val TAG = "TheoryFeedbackDialogFragment"

        fun newInstance(): TheoryFeedbackDialogFragment =
            TheoryFeedbackDialogFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val viewModel: TheoryFeedbackViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        HyperskillApp.graph().buildPlatformTheoryFeedbackComponent().reduxViewModelFactory
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return createDialog(TODO()).apply {
            setOnShowListener {
                TODO("Send shown message")
            }
        }
    }

    private fun createDialog(content: View): AlertDialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.theory_feedback_alert_title)
            .setView(content)
            .setPositiveButton(R.string.theory_feedback_alert_send_button) { _, _ ->
                TODO()
            }
            .setNegativeButton(R.string.theory_feedback_alert_cancel_button, null)
            .create()

    override fun onCancel(dialog: DialogInterface) {
        TODO("Send hidden message")
    }

    override fun onAction(action: TheoryFeedbackFeature.Action.ViewAction) {
        TODO("Not yet implemented")
    }

    override fun render(state: TheoryFeedbackFeature.ViewState) {
        TODO("Not yet implemented")
    }
}