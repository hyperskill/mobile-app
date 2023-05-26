package org.hyperskill.app.android.projects_selection.details.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ViewState
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsViewModel
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class ProjectSelectionDetailsFragment : Fragment(), ReduxView<ViewState, ViewAction> {

    companion object {
        fun newInstance(params: ProjectSelectionDetailsParams): ProjectSelectionDetailsFragment =
            ProjectSelectionDetailsFragment().apply {
                this.params = params
            }
    }

    private var params: ProjectSelectionDetailsParams by argument(ProjectSelectionDetailsParams.serializer())

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val projectSelectionDetailsViewModel: ProjectSelectionDetailsViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory) { "ViewModelFactory must be initialized" }
    }

    private val mainScreenRouter: MainScreenRouter by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectSelectionDetailsViewModel.onNewMessage(Message.Initialize)
    }

    private fun injectComponents() {
        val platformProjectSelectListComponent =
            HyperskillApp.graph().buildPlatformProjectSelectionDetailsComponent(params)
        viewModelFactory = platformProjectSelectListComponent.reduxViewModelFactory
    }

    override fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.NavigateTo.StudyPlan -> {
                requireRouter().backTo(MainScreen)
                mainScreenRouter.switch(StudyPlanScreen)
            }
            ViewAction.ShowProjectSelectionStatus.Success ->
                view?.snackbar(org.hyperskill.app.R.string.project_selection_details_project_selection_success_message)
            ViewAction.ShowProjectSelectionStatus.Error ->
                view?.snackbar(org.hyperskill.app.R.string.project_selection_details_project_selection_error_message)
        }
    }

    override fun render(state: ViewState) {
        TODO("Not yet implemented")
    }
}