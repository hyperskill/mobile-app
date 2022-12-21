package org.hyperskill.app.android.core.view.ui.fragment

import androidx.annotation.IdRes
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

/**
 * Set fragment built by [buildFragment] into [containerViewId] using Fragment.childFragmentManager
 * */
fun Fragment.setChildFragment(
    @IdRes containerViewId: Int,
    tag: String,
    buildFragment: () -> Fragment
) {
    if (childFragmentManager.findFragmentByTag(tag) == null) {
        val fragment = buildFragment()
        childFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, tag)
            .commitNow()
    }
}