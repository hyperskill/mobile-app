package org.hyperskill.app.project_selection.details.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProjectSelectionDetailsComponentImpl(
    private val projectSelectionDetailsComponent: ProjectSelectionDetailsComponent,
    private val params: ProjectSelectionDetailsParams
) : PlatformProjectSelectionDetailsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                ProjectSelectionDetailsViewModel::class.java to {
                    ProjectSelectionDetailsViewModel(
                        reduxViewContainer = projectSelectionDetailsComponent
                            .projectSelectionDetailsFeature(params)
                            .wrapWithViewContainer()
                    )
                }
            )
        )
}