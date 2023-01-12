package org.hyperskill

import org.hyperskill.app.step_quiz_hints.mapper.StepQuizHintsViewStateMapper
import org.hyperskill.app.step_quiz_hints.model.StepQuizHintsViewState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.junit.Test
import kotlin.test.assertIs

class StepQuizHintsViewStateMapperTest {
    @Test
    fun `test idle view state when empty hintIds and current hint is null`() {
        val featureState = StepQuizHintsFeature.State.Content(
            hintsIds = emptyList(),
            currentHint = null,
            hintHasReaction = false,
            isDailyStep = false,
            stepId = 0L
        )
        assertIs<StepQuizHintsViewState.Idle>(StepQuizHintsViewStateMapper.mapState(featureState))
    }
}