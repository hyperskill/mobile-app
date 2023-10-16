package org.hyperskill.app.step_quiz_fill_blanks.presentation

import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksData

fun FillBlanksItemMapper.map(dataset: Dataset, reply: Reply?): FillBlanksData? =
    dataset.components?.let {
        mapInternal(
            componentsDataset = it,
            replyBlanks = reply?.blanks
        )
    }