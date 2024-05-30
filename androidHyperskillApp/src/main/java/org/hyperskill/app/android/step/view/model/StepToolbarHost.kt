package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature

interface StepToolbarHost {
    fun renderToolbarContent(viewState: StepToolbarContentViewState)
    fun renderTopicProgress(viewState: StepToolbarFeature.ViewState)
    fun renderTheoryAction(action: OpenTheoryMenuAction)
    fun renderSecondaryMenuActions(actions: Set<StepMenuAction>)
}