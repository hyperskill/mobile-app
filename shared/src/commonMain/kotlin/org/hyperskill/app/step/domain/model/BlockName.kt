package org.hyperskill.app.step.domain.model

// TODO: ALTAPPS-569 Refactor BlockName to enum class
object BlockName {
    const val CHOICE = "choice"
    const val CODE = "code"
    const val MATCHING = "matching"
    const val MATH = "math"
    const val NUMBER = "number"
    const val PYCHARM = "pycharm"
    const val SORTING = "sorting"
    const val SQL = "sql"
    const val STRING = "string"
    const val TABLE = "table"
    const val TEXT = "text"
    const val PARSONS = "parsons"
    const val FILL_BLANKS = "fill-blanks"
    const val PROMPT = "prompt"

    val codeRelatedBlocksNames: Set<String> =
        setOf(CODE, PYCHARM, SQL)
}

expect val BlockName.supportedBlocksNames: Set<String>