package org.hyperskill.app.android.core.view.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.R

class LoadingProgressDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(): DialogFragment =
            LoadingProgressDialogFragment()

        const val TAG = "LoadingProgressDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.loading)
            .setView(R.layout.dialog_progress)
            .setCancelable(false)
            .create()
    }
}