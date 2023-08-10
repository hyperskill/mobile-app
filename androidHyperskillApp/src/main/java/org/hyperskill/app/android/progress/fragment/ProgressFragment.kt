package org.hyperskill.app.android.progress.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.progress.ui.ProgressScreen
import org.hyperskill.app.android.projects_selection.list.navigation.ProjectSelectionListScreen
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.progress.presentation.ProgressScreenViewModel
import org.hyperskill.app.progress_screen.presentation.ProgressScreenFeature
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams

class ProgressFragment : Fragment() {

    companion object {
        fun newInstance(): ProgressFragment =
            ProgressFragment()
    }

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val progressScreenViewModel: ProgressScreenViewModel by viewModels { requireNotNull(viewModelFactory) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        progressScreenViewModel.handleActions(this, block = ::onAction)
    }

    private fun injectComponent() {
        val progressComponent = HyperskillApp.graph().buildPlatformProgressScreenComponent()
        viewModelFactory = progressComponent.reduxViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    ProgressScreen(
                        progressScreenViewModel,
                        onBackClick = { requireRouter().exit() }
                    )
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressScreenViewModel.onNewMessage(ProgressScreenFeature.Message.ViewedEventMessage)
    }

    private fun onAction(action: ProgressScreenFeature.Action.ViewAction) {
        when (action) {
            is ProgressScreenFeature.Action.ViewAction.NavigateTo.ProjectSelectionScreen ->
                requireRouter().navigateTo(
                    ProjectSelectionListScreen(
                        ProjectSelectionListParams(
                            trackId = action.trackId
                        )
                    )
                )
            ProgressScreenFeature.Action.ViewAction.NavigateTo.TrackSelectionScreen ->
                requireRouter().navigateTo(
                    TrackSelectionListScreen(
                        TrackSelectionListParams()
                    )
                )
        }
    }
}