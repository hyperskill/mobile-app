package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RequestDailyStudyReminderDialogFragment : DialogFragment() {

    companion object {
        const val TAG: String = "RequestDailyStudyReminderDialogFragment"

        fun newInstance(): RequestDailyStudyReminderDialogFragment =
            RequestDailyStudyReminderDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(org.hyperskill.app.R.string.after_daily_step_completed_dialog_title)
            .setMessage(org.hyperskill.app.R.string.after_daily_step_completed_dialog_text)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                (parentFragment as Callback).onPermissionResult(isGranted = true)
                dialog.dismiss()
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                (parentFragment as Callback).onPermissionResult(isGranted = false)
                dialog.dismiss()
            }
            .show()

    interface Callback {
        fun onPermissionResult(isGranted: Boolean)
    }
}