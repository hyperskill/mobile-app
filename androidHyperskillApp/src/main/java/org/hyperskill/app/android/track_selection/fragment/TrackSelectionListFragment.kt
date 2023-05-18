package org.hyperskill.app.android.track_selection.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentTrackSelectionListBinding
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.Action.ViewAction
import org.hyperskill.app.track_selection.presentation.TrackSelectionListFeature.ViewState
import org.hyperskill.app.track_selection.presentation.TrackSelectionListViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TrackSelectionListFragment : Fragment(R.layout.fragment_track_selection_list), ReduxView<ViewState, ViewAction> {
    companion object {
        fun newInstance() =
            TrackSelectionListFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val trackSelectionListViewModel: TrackSelectionListViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory) { "ViewModelFactory must be initialized" }
    }

    private val viewBinding: FragmentTrackSelectionListBinding by viewBinding(FragmentTrackSelectionListBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        viewModelFactory = HyperskillApp.graph().buildPlatformTrackSelectionListComponent().reduxViewModelFactory
    }

    override fun onAction(action: ViewAction) {
        TODO("Not yet implemented")
    }

    override fun render(state: ViewState) {
        TODO("Not yet implemented")
    }
}