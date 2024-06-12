package org.hyperskill.app.android.core.extensions

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.view.HapticFeedbackConstantsCompat

fun View.performRejectHapticFeedback(flag: Int = 0): Boolean =
    performHapticFeedback(HapticFeedbackConstantsCompat.REJECT, flag = flag)

fun View.performConfirmHapticFeedback(flag: Int = 0): Boolean =
    performHapticFeedback(HapticFeedbackConstantsCompat.CONFIRM, flag = flag)

// If REJECT feedback was not performed, then use default to haptic feedback
@Suppress("DEPRECATION")
fun View.performHapticFeedback(
    feedbackConstant: Int,
    flag: Int = 0,
    defaultFeedbackConstant: Int = HapticFeedbackConstantsCompat.LONG_PRESS
): Boolean =
    performHapticFeedback(
        /* feedbackConstant = */ feedbackConstant,
        /* flags = */ HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING or flag
    ) || performHapticFeedback(defaultFeedbackConstant)