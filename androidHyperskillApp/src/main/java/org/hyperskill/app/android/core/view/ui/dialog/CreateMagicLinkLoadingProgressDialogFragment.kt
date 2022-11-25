package org.hyperskill.app.android.core.view.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.R

class CreateMagicLinkLoadingProgressDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(): DialogFragment =
            CreateMagicLinkLoadingProgressDialogFragment()

        const val TAG = "CreateMagicLinkLoadingProgressDialogFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false

        return MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_magic_link_progress)
            .setCancelable(false)
            .create()
    }
}