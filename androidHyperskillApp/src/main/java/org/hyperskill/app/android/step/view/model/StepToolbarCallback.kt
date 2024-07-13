package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction

interface StepToolbarCallback {
    fun onPrimaryActionClicked(action: StepMenuPrimaryAction)
    fun onSecondaryActionClicked(action: StepMenuSecondaryAction)
}