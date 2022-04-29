package org.hyperskill.app.android.core.view.ui.fragment

import androidx.fragment.app.Fragment

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.parentOfType(klass: Class<T>): T? =
    parentFragment
        ?.let { parent ->
            if (klass.isAssignableFrom(parent.javaClass)) {
                parent as T
            } else {
                parent.parentOfType(klass)
            }
        }
        ?: activity?.let { activity ->
            if (klass.isAssignableFrom(activity.javaClass)) {
                activity as T
            } else {
                null
            }
        }