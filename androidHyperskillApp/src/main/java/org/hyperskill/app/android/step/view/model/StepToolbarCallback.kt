package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepToolbarAction

interface StepToolbarCallback {
    fun onActionClicked(action: StepToolbarAction)
}