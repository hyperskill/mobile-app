package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertIs
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.view.mapper.StepQuizHintsViewStateMapper

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
        assertIs<StepQuizHintsFeature.ViewState.Idle>(StepQuizHintsViewStateMapper.mapState(featureState))
    }
}