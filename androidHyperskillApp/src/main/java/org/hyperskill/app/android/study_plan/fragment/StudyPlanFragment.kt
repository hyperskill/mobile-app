package org.hyperskill.app.android.study_plan.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireMainRouter
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentStudyPlanBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.stage_implementation.view.navigation.StageImplementationScreen
import org.hyperskill.app.android.study_plan.delegate.StudyPlanWidgetDelegate
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.study_plan.presentation.StudyPlanScreenViewModel
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StudyPlanFragment :
    Fragment(R.layout.fragment_study_plan),
    ReduxView<StudyPlanScreenViewState, StudyPlanScreenFeature.Action.ViewAction> {

    companion object {
        fun newInstance(): StudyPlanFragment =
            StudyPlanFragment()
    }

    private val viewBinding: FragmentStudyPlanBinding by viewBinding(FragmentStudyPlanBinding::bind)

    private lateinit var viewModelFactory: ReduxViewModelFactory
    private val studyPlanViewModel: StudyPlanScreenViewModel by reduxViewModel(this) { viewModelFactory }

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null
    private var studyPlanWidgetDelegate: StudyPlanWidgetDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        studyPlanWidgetDelegate = StudyPlanWidgetDelegate(
            context = requireContext(),
            onNewMessage = {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.StudyPlanWidgetMessage(it))
            }
        )
    }

    private fun injectComponents() {
        val studyPlanScreeComponent =
            HyperskillApp.graph().buildPlatformStudyPlanScreenComponent()
        viewModelFactory = studyPlanScreeComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initGamificationToolbarDelegate()
        studyPlanWidgetDelegate?.setup(viewBinding.studyPlanRecycler, viewBinding.studyPlanError)
    }

    private fun initGamificationToolbarDelegate() {
        viewBinding.studyPlanAppBar.gamificationToolbar.title =
            requireContext().getString(org.hyperskill.app.R.string.study_plan_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            viewLifecycleOwner,
            requireContext(),
            viewBinding.studyPlanAppBar,
            GamificationToolbarScreen.STUDY_PLAN
        ) { message ->
            studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.GamificationToolbarMessage(message))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        studyPlanWidgetDelegate?.cleanup()
        gamificationToolbarDelegate = null
    }

    override fun onDestroy() {
        studyPlanWidgetDelegate = null
        super.onDestroy()
    }

    override fun render(state: StudyPlanScreenViewState) {
        gamificationToolbarDelegate?.render(state.toolbarState)
        gamificationToolbarDelegate?.setSubtitle(state.trackTitle)
        studyPlanWidgetDelegate?.render(state.studyPlanWidgetViewState)
    }

    override fun onAction(action: StudyPlanScreenFeature.Action.ViewAction) {
        when (action) {
            is StudyPlanScreenFeature.Action.ViewAction.GamificationToolbarViewAction -> {
                when (action.viewAction) {
                    GamificationToolbarFeature.Action.ViewAction.ShowProfileTab -> {
                        requireMainRouter().switch(ProfileScreen(isInitCurrent = true))
                    }
                }
            }
            is StudyPlanScreenFeature.Action.ViewAction.StudyPlanWidgetViewAction -> {
                when (val viewAction = action.viewAction) {
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StageImplementation -> {
                        requireRouter().navigateTo(
                            StageImplementationScreen(
                                projectId = viewAction.projectId,
                                stageId = viewAction.stageId
                            )
                        )
                    }
                }
            }
        }
    }
}