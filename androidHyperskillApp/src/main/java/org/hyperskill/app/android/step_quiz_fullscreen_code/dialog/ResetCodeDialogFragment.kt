package org.hyperskill.app.android.step_quiz_fullscreen_code.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.R

class ResetCodeDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(): ResetCodeDialogFragment =
            ResetCodeDialogFragment()
    }

    interface Callback {
        fun onReset()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.reset_code_dialog_title)
            .setMessage(R.string.reset_code_dialog_explanation)
            .setPositiveButton(R.string.yes) { _, _ ->
                (parentFragment as Callback).onReset()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
}
