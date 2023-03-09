package org.hyperskill.app.android.step_quiz_code.view.model

import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.android.step_quiz_code.view.model.config.CommonCodeQuizConfig
import org.hyperskill.app.android.step_quiz_code.view.model.config.PycharmCodeQuizConfig
import org.hyperskill.app.android.step_quiz_code.view.model.config.SqlCodeStepConfig
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step

object CodeStepQuizConfigFactory {
    fun create(step: Step): CodeStepQuizConfig =
        when (val block = step.block.name) {
            BlockName.CODE -> CommonCodeQuizConfig(step)
            BlockName.SQL -> SqlCodeStepConfig(step)
            BlockName.PYCHARM -> PycharmCodeQuizConfig(step)
            else -> error("Unsupported code quiz type: $block")
        }
}