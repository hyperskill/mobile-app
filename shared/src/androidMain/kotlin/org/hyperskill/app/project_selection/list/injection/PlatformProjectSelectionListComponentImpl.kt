package org.hyperskill.app.project_selection.list.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.list.presentation.ProjectSelectionListViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformProjectSelectionListComponentImpl(
    private val projectSelectionListComponent: ProjectSelectionListComponent,
    private val params: ProjectSelectionListParams
) : PlatformProjectSelectionListComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                ProjectSelectionListViewModel::class.java to {
                    ProjectSelectionListViewModel(
                        projectSelectionListComponent
                            .projectSelectionListFeature(params)
                            .wrapWithViewContainer()
                    )
                }
            )
        )
}