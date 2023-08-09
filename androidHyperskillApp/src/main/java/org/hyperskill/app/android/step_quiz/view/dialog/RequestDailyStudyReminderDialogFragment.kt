package org.hyperskill.app.android.step_quiz.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.nobird.android.view.base.ui.extension.argument

class RequestDailyStudyReminderDialogFragment : DialogFragment() {

    companion object {
        const val TAG: String = "RequestDailyStudyReminderDialogFragment"

        fun newInstance(title: String, message: String): RequestDailyStudyReminderDialogFragment =
            RequestDailyStudyReminderDialogFragment().apply {
                this.title = title
                this.message = message
            }
    }

    private var title: String by argument()
    private var message: String by argument()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
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