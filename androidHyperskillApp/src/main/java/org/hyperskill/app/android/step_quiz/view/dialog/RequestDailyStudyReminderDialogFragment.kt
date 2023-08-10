package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.nobird.android.view.base.ui.extension.argument

class RequestDailyStudyReminderDialogFragment : DialogFragment() {

    companion object {
        const val TAG: String = "RequestDailyStudyReminderDialogFragment"

        fun newInstance(onPermissionResult: (Boolean) -> Unit): RequestDailyStudyReminderDialogFragment =
            RequestDailyStudyReminderDialogFragment().apply {
                this.onPermissionResult = onPermissionResult
            }
    }

    private var onPermissionResult: (Boolean) -> Unit by argument()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.hyperskill.app.R.string.after_daily_step_completed_dialog_title)
            .setMessage(org.hyperskill.app.R.string.after_daily_step_completed_dialog_text)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                onPermissionResult(true)
                dialog.dismiss()
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                onPermissionResult(false)
                dialog.dismiss()
            }
            .show()
}