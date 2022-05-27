package org.hyperskill.app.core.injection

import org.hyperskill.app.auth.injection.AuthComponentManual
import org.hyperskill.app.main.injection.MainComponentManual
import org.hyperskill.app.step.injection.StepComponentManual

interface AppGraph {
    val commonComponent: CommonComponent
    val mainComponentManual: MainComponentManual
    val networkComponentManual: NetworkComponentManual
    val authComponentManual: AuthComponentManual

    fun buildStepComponent(): StepComponentManual
}