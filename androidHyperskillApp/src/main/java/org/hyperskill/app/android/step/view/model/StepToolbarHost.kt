package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature

interface StepToolbarHost {
    fun renderToolbarContent(viewState: StepToolbarContentViewState)
    fun renderTopicProgress(viewState: StepToolbarFeature.ViewState)
    fun renderPrimaryAction(action: StepMenuPrimaryAction, params: StepMenuPrimaryActionParams)
    fun renderSecondaryMenuActions(actions: Set<StepMenuSecondaryAction>)
}