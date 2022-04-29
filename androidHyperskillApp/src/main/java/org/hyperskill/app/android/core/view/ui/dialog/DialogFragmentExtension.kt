package org.hyperskill.app.android.core.view.ui.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.dismissIfExists(manager: FragmentManager, tag: String) {
    if (manager.findFragmentByTag(tag) != null) {
        dismiss()
    }
}