package org.hyperskill.app.android.topics_repetitions.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentTopicsRepetitionBinding
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionViewModel
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TopicsRepetitionFragment :
    Fragment(R.layout.fragment_topics_repetition),
    ReduxView<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            TopicsRepetitionFragment()
    }

    private val viewBinding: FragmentTopicsRepetitionBinding by viewBinding(FragmentTopicsRepetitionBinding::bind)

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val topicsRepetitionViewModel: TopicsRepetitionViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        viewModelFactory = HyperskillApp.graph().buildPlatformTopicsRepetitionsComponent().reduxViewModel
    }

    override fun onAction(action: TopicsRepetitionsFeature.Action.ViewAction) {
        /*TODO("Not yet implemented")*/
    }

    override fun render(state: TopicsRepetitionsFeature.State) {
        /*TODO("Not yet implemented")*/
    }
}