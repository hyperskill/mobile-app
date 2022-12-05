package org.hyperskill.app.android.topics_repetitions.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentTopicsRepetitionBinding
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionHeaderDelegate
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionListDelegate
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionViewModel
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class TopicsRepetitionFragment :
    Fragment(R.layout.fragment_topics_repetition),
    ReduxView<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Action.ViewAction> {
    companion object {
        fun newInstance(recommendedRepetitionsCount: Int): Fragment =
            TopicsRepetitionFragment().apply {
                this.recommendedRepetitionsCount = recommendedRepetitionsCount
            }
    }

    private var recommendedRepetitionsCount: Int by argument()

    private val viewBinding: FragmentTopicsRepetitionBinding by viewBinding(FragmentTopicsRepetitionBinding::bind)
    private var viewStateDelegate: ViewStateDelegate<TopicsRepetitionsFeature.State>? = null

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val topicsRepetitionViewModel: TopicsRepetitionViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private var viewDataMapper: TopicsRepetitionsViewDataMapper? = null

    private var topicsRepetitionListDelegate: TopicsRepetitionListDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val platformTopicsRepetitionsComponent =
            HyperskillApp.graph().buildPlatformTopicsRepetitionsComponent(recommendedRepetitionsCount)
        viewModelFactory = platformTopicsRepetitionsComponent.reduxViewModelFactory
        viewDataMapper = platformTopicsRepetitionsComponent.topicsRepetitionsViewDataMapper
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        topicsRepetitionListDelegate = TopicsRepetitionListDelegate(
            viewBinding.topicsList,
            onNewMessage = topicsRepetitionViewModel::onNewMessage
        )
        with(viewBinding) {
            homeScreenError.tryAgain.setOnClickListener {
                topicsRepetitionViewModel.onNewMessage(
                    TopicsRepetitionsFeature.Message.Initialize(
                        recommendedRepetitionsCount = recommendedRepetitionsCount,
                        forceUpdate = true
                    )
                )
            }
        }
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<TopicsRepetitionsFeature.State>().apply {
            addState<TopicsRepetitionsFeature.State.Idle>()
            addState<TopicsRepetitionsFeature.State.Loading>(viewBinding.topicsRepetitionProgress)
            addState<TopicsRepetitionsFeature.State.NetworkError>(viewBinding.homeScreenError.root)
            addState<TopicsRepetitionsFeature.State.Content>(
                viewBinding.topicsRepetitionAppBar,
                viewBinding.topicsRepetitionContentNestedScroll
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
        topicsRepetitionListDelegate = null
    }

    override fun onAction(action: TopicsRepetitionsFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: TopicsRepetitionsFeature.State) {
        viewStateDelegate?.switchState(state)
        val mapper = viewDataMapper
        if (mapper != null && state is TopicsRepetitionsFeature.State.Content) {
            viewLifecycleOwner.lifecycleScope.launch {
                val viewState = withContext(Dispatchers.Default) {
                    mapper.mapStateToViewData(state)
                }
                TopicsRepetitionHeaderDelegate.render(
                    requireContext(),
                    viewBinding.topicsRepetitionHeader,
                    viewState.repetitionsStatus
                )
                topicsRepetitionListDelegate?.render(
                    repeatBlockTitle = viewState.repeatBlockTitle,
                    trackTopicsTitle = viewState.trackTopicsTitle,
                    topicsToRepeat = viewState.topicsToRepeat,
                    topicsToRepeatWillLoadedCount = viewState.topicsToRepeatWillLoadedCount,
                    showMoreButtonState = viewState.showMoreButtonState
                )
            }
        }
    }
}