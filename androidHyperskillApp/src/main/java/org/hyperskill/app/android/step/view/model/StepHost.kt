package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step.domain.model.StepRoute

interface StepHost {
    fun reloadStep(stepRoute: StepRoute)
    fun navigateToTheory(stepRoute: StepRoute)
}