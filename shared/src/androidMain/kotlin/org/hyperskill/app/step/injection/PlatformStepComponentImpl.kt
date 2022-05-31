package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.ManualViewModelFactory
import org.hyperskill.app.step.presentation.StepViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepComponentImpl(private val stepComponent: StepComponent) : PlatformStepComponent {
    override val manualViewModelFactory: ManualViewModelFactory
        get() = ManualViewModelFactory(mapOf(StepViewModel::class.java to { StepViewModel(stepComponent.stepFeature.wrapWithViewContainer()) }))
}