package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.step_quiz_code.view.adapter.CodeDetailSampleAdapterDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.android.ui.custom.ArrowImageView
import org.hyperskill.app.android.view.base.ui.extension.collapse
import org.hyperskill.app.android.view.base.ui.extension.expand
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class CodeQuizInstructionDelegate(
    private val detailsContainerView: View,
    isCollapsible: Boolean,
    onDetailsIsExpandedStateChanged: () -> Unit
) {
    private val stepQuizCodeDetails: FrameLayout =
        detailsContainerView.findViewById(R.id.stepQuizCodeDetailsFrameLayout)
    private val stepQuizCodeDetailsArrow: ArrowImageView =
        detailsContainerView.findViewById(R.id.stepQuizCodeDetailsArrow)
    private val stepQuizCodeDetailsContent: RecyclerView =
        detailsContainerView.findViewById(R.id.stepQuizCodeDetailsContent)

    private val stepQuizCodeDetailsAdapter = DefaultDelegateAdapter<CodeDetail>()

    init {
        stepQuizCodeDetailsAdapter += CodeDetailSampleAdapterDelegate()

        with(stepQuizCodeDetailsContent) {
            layoutManager = LinearLayoutManager(context)
            adapter = stepQuizCodeDetailsAdapter
            isNestedScrollingEnabled = false

            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider.setDrawable(AppCompatResources.getDrawable(context, R.drawable.bg_divider_vertical)!!)
            addItemDecoration(divider)
        }

        if (isCollapsible) {
            stepQuizCodeDetails.setOnClickListener {
                stepQuizCodeDetailsArrow.changeState()
                if (stepQuizCodeDetailsArrow.isExpanded()) {
                    stepQuizCodeDetailsContent.expand()
                } else {
                    stepQuizCodeDetailsContent.collapse()
                }
                onDetailsIsExpandedStateChanged()
            }
        } else {
            stepQuizCodeDetailsArrow.isVisible = false
            stepQuizCodeDetailsContent.isVisible = true
        }
    }

    fun setCodeDetailsData(details: List<CodeDetail>, lang: String?) {
        if (lang == ProgrammingLanguage.SQL.serverPrintableName) {
            detailsContainerView.isVisible = false
        } else {
            stepQuizCodeDetailsAdapter.items = details
            detailsContainerView.isVisible = details.isNotEmpty()
        }
    }
}