package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.ManualViewModelFactory
import org.hyperskill.app.main.presentation.MainViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformMainComponentImpl(private val mainComponent: MainComponent) : PlatformMainComponent {
    override val manualViewModelFactory: ManualViewModelFactory
        get() = ManualViewModelFactory(mapOf(MainViewModel::class.java to { MainViewModel(mainComponent.appFeature.wrapWithViewContainer()) }))
}