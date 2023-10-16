package org.hyperskill.app.step_quiz_fill_blanks.model

data class FillBlanksData(
    val fillBlanks: List<FillBlanksItem>,
    val language: String?
)