package org.hyperskill.step

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFalse
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.view.mapper.StepViewStateMapper
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature
import org.hyperskill.step.domain.model.stub

class StepViewStateMapperTest {
    @Test
    fun `Comments secondary action should be presented when comments are present`() {
        val step = Step.stub(
            id = 1,
            commentsStatistics = listOf(
                CommentStatisticsEntry(
                    thread = CommentThread.COMMENT,
                    totalCount = 1
                )
            )
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val state = stubState(stepState = stubStepDataState(step, stepRoute))

        val mapper = StepViewStateMapper(stepRoute)
        val viewState = mapper.map(state)

        assertContains(viewState.stepMenuSecondaryActions, StepMenuSecondaryAction.СOMMENTS)
    }

    @Test
    fun `Comments secondary action should not be presented when no comments are present`() {
        val step = Step.stub(
            id = 1,
            commentsStatistics = listOf(
                CommentStatisticsEntry(
                    thread = CommentThread.COMMENT,
                    totalCount = 0
                )
            )
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val state = stubState(stepState = stubStepDataState(step, stepRoute))

        val mapper = StepViewStateMapper(stepRoute)
        val viewState = mapper.map(state)

        assertFalse(viewState.stepMenuSecondaryActions.contains(StepMenuSecondaryAction.СOMMENTS))
    }

    @Test
    fun `stepMenuActions contains SKIP when skip is available`() {
        val step = Step.stub(
            id = 1,
            canSkip = true
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val state = stubState(stepState = stubStepDataState(step, stepRoute))

        val mapper = StepViewStateMapper(stepRoute)
        val viewState = mapper.map(state)

        assertContains(viewState.stepMenuSecondaryActions, StepMenuSecondaryAction.SKIP)
    }

    @Test
    fun `stepMenuActions does not include SKIP when can not skip`() {
        val step = Step.stub(
            id = 1,
            canSkip = false
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val state = stubState(stepState = stubStepDataState(step, stepRoute))

        val mapper = StepViewStateMapper(stepRoute)
        val viewState = mapper.map(state)

        assertFalse(viewState.stepMenuSecondaryActions.contains(StepMenuSecondaryAction.SKIP))
    }

    @Test
    fun `stepMenuActions does not include SKIP when StepRoute is not Learn`() {
        val step = Step.stub(
            id = 1,
            canSkip = true
        )
        val stepRoute = StepRoute.LearnDaily(1L)
        val state = stubState(stepState = stubStepDataState(step, stepRoute))

        val mapper = StepViewStateMapper(stepRoute)
        val viewState = mapper.map(state)

        assertFalse(viewState.stepMenuSecondaryActions.contains(StepMenuSecondaryAction.SKIP))
    }

    private fun stubStepDataState(
        step: Step,
        stepRoute: StepRoute,
        isPracticingAvailable: Boolean = false
    ): StepFeature.StepState.Data =
        StepFeature.StepState.Data(
            step = step,
            isPracticingAvailable = isPracticingAvailable,
            stepCompletionState = StepCompletionFeature.createState(
                step = step,
                stepRoute = stepRoute
            )
        )

    private fun stubState(
        stepState: StepFeature.StepState,
        stepToolbarState: StepToolbarFeature.State = StepToolbarFeature.State.Idle,
        isLoadingShowed: Boolean = false
    ): StepFeature.State =
        StepFeature.State(
            stepState = stepState,
            stepToolbarState = stepToolbarState,
            isLoadingShowed = isLoadingShowed
        )
}