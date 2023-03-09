package org.hyperskill.app.android.step_quiz_code.view.model

import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step

object CodeStepQuizConfigFactory {
    fun create(step: Step): CodeStepQuizConfig =
        when (val block = step.block.name) {
            BlockName.CODE -> CommonCodeQuizConfig(step)
            BlockName.PYCHARM -> PycharmCodeQuizConfig(step)
            else -> error("Unsupported code quiz type: $block")
        }
}