package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step.presentation.StepViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepComponentImpl(private val stepComponent: StepComponent) : PlatformStepComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(StepViewModel::class.java to { StepViewModel(stepComponent.stepFeature.wrapWithViewContainer()) })
        )
}