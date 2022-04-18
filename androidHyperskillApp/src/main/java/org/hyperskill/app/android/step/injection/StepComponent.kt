package org.hyperskill.app.android.step.injection

import dagger.Subcomponent
import org.hyperskill.app.android.step.view.ui.fragment.StepFragment

@Subcomponent(
    modules = [
        StepModule::class,
        StepDataModule::class
    ]
)
interface StepComponent {
    @Subcomponent.Builder
    interface Builder {
        fun build(): StepComponent
    }
    fun inject(stepFragment: StepFragment)
}