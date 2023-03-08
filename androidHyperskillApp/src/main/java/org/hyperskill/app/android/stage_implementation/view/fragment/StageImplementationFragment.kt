package org.hyperskill.app.android.stage_implementation.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.databinding.FragmentStageImplentationBinding
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature
import org.hyperskill.app.stage_implementation.presentation.StageImplementationViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StageImplementationFragment :
    Fragment(R.layout.fragment_stage_implementation),
    ReduxView<StageImplementFeature.ViewState, StageImplementFeature.Action.ViewAction> {

    companion object {
        private const val STEP_TAG = "StageImplementationStepTag"

        fun newInstance(
            projectId: Long,
            stageId: Long
        ): StageImplementationFragment =
            StageImplementationFragment().apply {
                this.projectId = projectId
                this.stageId = stageId
            }
    }

    private val viewBinding: FragmentStageImplementationBinding by viewBinding(
        FragmentStageImplementationBinding::bind
    )

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val stageImplementationViewModel: StageImplementationViewModel by reduxViewModel(this) {
        requireNotNull(viewModelFactory)
    }

    private var projectId: Long by argument()
    private var stageId: Long by argument()

    private var viewStateDelegate: ViewStateDelegate<StageImplementFeature.ViewState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val stageImplementationComponent =
            HyperskillApp.graph().buildPlatformStageImplementationComponent(
                projectId = projectId,
                stageId = stageId
            )
        viewModelFactory = stageImplementationComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewStateDelegate = ViewStateDelegate<StageImplementFeature.ViewState>().apply {
            addState<StageImplementFeature.ViewState.Idle>()
            addState<StageImplementFeature.ViewState.Loading>(viewBinding.stageProgress)
            addState<StageImplementFeature.ViewState.NetworkError>(viewBinding.stageError.root)
            addState<StageImplementFeature.ViewState.Content>(viewBinding.stageContainer)
        }
        viewBinding.stageError.tryAgain.setOnClickListener {
            stageImplementationViewModel.onNewMessage(
                StageImplementFeature.Message.Initialize(projectId = projectId, stageId = stageId, forceUpdate = true)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewStateDelegate = null
    }

    override fun onAction(action: StageImplementFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: StageImplementFeature.ViewState) {
        when (state) {
            is StageImplementFeature.ViewState.Content -> TODO()
            StageImplementFeature.ViewState.Idle -> TODO()
            StageImplementFeature.ViewState.Loading -> TODO()
            StageImplementFeature.ViewState.NetworkError -> TODO()
        }
    }
}