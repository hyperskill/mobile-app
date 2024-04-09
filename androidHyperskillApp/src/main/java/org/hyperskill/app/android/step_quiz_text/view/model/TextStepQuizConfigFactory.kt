package org.hyperskill.app.android.step_quiz_text.view.model

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step

object TextStepQuizConfigFactory {
    fun create(step: Step): TextStepQuizConfig =
        when (val block = step.block.name) {
            BlockName.STRING -> PlainTextStepQuizConfig
            BlockName.NUMBER -> NumberStepQuizConfig
            BlockName.MATH -> MathStepQuizConfig
            else -> error("Unsupported block type = $block")
        }
}