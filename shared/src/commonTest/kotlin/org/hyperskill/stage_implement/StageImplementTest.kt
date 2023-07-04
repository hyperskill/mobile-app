package org.hyperskill.stage_implement

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Action
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.Message
import org.hyperskill.app.stage_implement.presentation.StageImplementFeature.State
import org.hyperskill.app.stage_implement.presentation.StageImplementReducer
import org.hyperskill.app.stages.domain.model.Stage

class StageImplementTest {
    private val stageImplementReducer = StageImplementReducer(
        HyperskillAnalyticRoute.Projects.Stages.Implement(projectId = 0, stageId = 0)
    )

    @Test
    fun `Step solved message should trigger check completion status action if solved step is stage step`() {
        val stepId = 1L
        val stage = Stage.stub(stepId = stepId)
        val (_, actions) = stageImplementReducer.reduce(
            State.Content(0, stage),
            Message.StepSolved(stepId)
        )

        assertContains(actions, Action.CheckStageCompletionStatus(stage))
    }

    @Test
    fun `Step solved message should NOT trigger check completion status action if solved step is NOT stage step`() {
        val stageStepId = 1L
        val solvedStepId = 2L
        val stage = Stage.stub(stepId = stageStepId)
        val (_, actions) = stageImplementReducer.reduce(
            State.Content(0, stage),
            Message.StepSolved(solvedStepId)
        )

        assertTrue {
            actions.none { it is Action.CheckStageCompletionStatus }
        }
    }

    @Test
    fun `Stage completed message should trigger show stage completed modal view action`() {
        val (_, actions) = stageImplementReducer.reduce(
            State.Content(0, Stage.stub()),
            Message.StageCompleted("", 0)
        )

        assertTrue {
            actions.any { it is Action.ViewAction.ShowStageCompletedModal }
        }
    }

    @Test
    fun `Project completed message should trigger show project completed modal view action`() {
        val (_, actions) = stageImplementReducer.reduce(
            State.Content(0, Stage.stub()),
            Message.ProjectCompleted(0, 0)
        )

        assertTrue {
            actions.any { it is Action.ViewAction.ShowProjectCompletedModal }
        }
    }
}