package org.hyperskill.app.android.step_quiz_code.view.mapper

import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.step.domain.model.Step

class CodeStepQuizDetailsMapper {
    fun mapToCodeDetails(step: Step): List<CodeDetail> =
        step.block.options
            .samples!!
            .mapIndexed { i, sample ->
                CodeDetail.Sample(
                    i + 1,
                    sample.first().trim('\n'),
                    sample.last().trim('\n')
                )
            }
}