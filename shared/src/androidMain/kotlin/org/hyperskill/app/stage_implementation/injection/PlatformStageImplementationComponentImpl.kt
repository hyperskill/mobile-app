package org.hyperskill.app.stage_implementation.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.stage_implement.injection.StageImplementComponent
import org.hyperskill.app.stage_implementation.presentation.StageImplementationViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStageImplementationComponentImpl(
    private val projectId: Long,
    private val stageId: Long,
    private val stageImplementationComponent: StageImplementComponent
) : PlatformStageImplementationComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                StageImplementationViewModel::class.java to {
                    StageImplementationViewModel(
                        projectId = projectId,
                        stageId = stageId,
                        reduxViewContainer = stageImplementationComponent.stageImplementFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}