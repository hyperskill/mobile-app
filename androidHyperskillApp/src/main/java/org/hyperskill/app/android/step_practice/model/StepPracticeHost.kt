package org.hyperskill.app.android.step_practice.model

import android.view.View

interface StepPracticeHost {
    fun fullScrollDown()
    fun scrollTo(view: View)
}