package org.hyperskill.app.android.step_quiz_fill_blanks.model

/**
 * Represents the state of the select mode in a fill in the blanks quiz.
 *
 * @property options The list of processed options for the select mode.
 * @property selectBlankToSelectOptionMap The map that associates blank indices with selected option indices.
 * @property selectItemIndices The list of indices of the select items in the fill in the blanks quiz.
 * @property highlightedSelectItemIndex The index of the currently highlighted select item.
 */
data class SelectState(
    val options: List<FillBlanksProcessedOption>,
    val selectBlankToSelectOptionMap: Map<Int, Int>,
    val selectItemIndices: List<Int>,
    val highlightedSelectItemIndex: Int?
)