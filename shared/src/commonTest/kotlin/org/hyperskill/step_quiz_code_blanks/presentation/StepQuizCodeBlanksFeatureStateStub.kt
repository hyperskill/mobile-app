package org.hyperskill.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.OnboardingState
import org.hyperskill.step.domain.model.stub

fun StepQuizCodeBlanksFeature.State.Content.Companion.stub(
    step: Step = Step.stub(id = 1),
    codeBlocks: List<CodeBlock> = emptyList(),
    onboardingState: OnboardingState = OnboardingState.Unavailable
): StepQuizCodeBlanksFeature.State.Content =
    StepQuizCodeBlanksFeature.State.Content(
        step = step,
        codeBlocks = codeBlocks,
        onboardingState = onboardingState
    )