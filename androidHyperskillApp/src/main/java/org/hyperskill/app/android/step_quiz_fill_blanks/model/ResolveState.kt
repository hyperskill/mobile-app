package org.hyperskill.app.android.step_quiz_fill_blanks.model

import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode

sealed class ResolveState {
    object NotResolved : ResolveState()
    data class ResolveSucceed(val mode: FillBlanksMode) : ResolveState()
    object ResolveFailed : ResolveState()
}