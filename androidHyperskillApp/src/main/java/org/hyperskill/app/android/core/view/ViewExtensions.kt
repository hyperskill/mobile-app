package org.hyperskill.app.android.core.view

import android.graphics.Rect
import android.view.View

const val PART_OF_KEYBOARD_ON_SCREEN = 0.15

inline fun setOnKeyboardOpenListener(
    rootView: View,
    crossinline onKeyboardShown: () -> Unit,
    crossinline onKeyboardHidden: () -> Unit
) {
    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)

        val screenHeight = rootView.rootView.height
        val keyboardHeight = screenHeight - rect.bottom

        if (keyboardHeight > screenHeight * PART_OF_KEYBOARD_ON_SCREEN) {
            onKeyboardShown()
        } else {
            onKeyboardHidden()
        }
    }
}