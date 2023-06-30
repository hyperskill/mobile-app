package org.hyperskill.app.progress.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel
import org.hyperskill.app.progresses.injection.ProgressScreenComponent

class PlatformProgressScreenComponentImpl(
    private val progressScreenComponent: ProgressScreenComponent
) : PlatformProgressScreenComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                ProgressScreenViewModel::class.java to {
                    ProgressScreenViewModel(progressScreenComponent.progressScreenFeature.wrapWithFlowView())
                }
            )
        )
}