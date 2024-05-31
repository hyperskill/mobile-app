package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepMenuAction

interface StepToolbarCallback {
    fun onActionClicked(action: StepMenuAction)
}