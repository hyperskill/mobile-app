package org.hyperskill.app.android.core.view.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.dismissDialogFragmentIfExists(tag: String) {
    (findFragmentByTag(tag) as? DialogFragment)?.dismiss()
}