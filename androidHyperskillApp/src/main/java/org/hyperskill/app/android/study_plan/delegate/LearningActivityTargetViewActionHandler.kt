package org.hyperskill.app.android.study_plan.delegate

import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.projects_selection.list.navigation.ProjectSelectionListScreen
import org.hyperskill.app.android.stage_implementation.view.dialog.UnsupportedStageBottomSheet
import org.hyperskill.app.android.stage_implementation.view.navigation.StageImplementationScreen
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.learning_activities.presentation.model.LearningActivityTargetViewAction
import org.hyperskill.app.project_selection.list.injection.ProjectSelectionListParams
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object LearningActivityTargetViewActionHandler {
    fun <TFragment> handle(
        fragment: TFragment,
        viewAction: LearningActivityTargetViewAction
    ) where TFragment : Fragment, TFragment : UnsupportedStageBottomSheet.Callback {
        when (viewAction) {
            is LearningActivityTargetViewAction.NavigateTo.SelectProject -> {
                fragment.requireRouter().navigateTo(
                    ProjectSelectionListScreen(
                        ProjectSelectionListParams(
                            trackId = viewAction.trackId,
                            isNewUserMode = false
                        )
                    )
                )
            }
            LearningActivityTargetViewAction.NavigateTo.SelectTrack -> {
                fragment.requireRouter().navigateTo(
                    TrackSelectionListScreen(
                        TrackSelectionListParams(isNewUserMode = false)
                    )
                )
            }
            is LearningActivityTargetViewAction.NavigateTo.StageImplement -> {
                fragment.requireRouter().navigateTo(
                    StageImplementationScreen(
                        projectId = viewAction.projectId,
                        stageId = viewAction.stageId
                    )
                )
            }
            is LearningActivityTargetViewAction.NavigateTo.Step -> {
                fragment.requireRouter().navigateTo(StepScreen(viewAction.stepRoute))
            }
            LearningActivityTargetViewAction.ShowStageImplementIDERequiredModal -> {
                UnsupportedStageBottomSheet.newInstance()
                    .showIfNotExists(fragment.childFragmentManager, UnsupportedStageBottomSheet.TAG)
            }
        }
    }
}