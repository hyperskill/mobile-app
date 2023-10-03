package org.hyperskill.app.android.step_quiz_code.view.delegate

import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.divider.MaterialDividerItemDecoration
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
    private val detailsCard: MaterialCardView =
        detailsContainerView.findViewById(R.id.stepQuizCodeDetailsMaterialCard)
    private val stepQuizCodeDetailsRecycler: RecyclerView =
        detailsContainerView.findViewById(R.id.stepQuizCodeDetailsRecycler)

    private val stepQuizCodeDetailsAdapter = DefaultDelegateAdapter<CodeDetail>()

    init {
        stepQuizCodeDetailsAdapter += CodeDetailSampleAdapterDelegate()

        with(stepQuizCodeDetailsRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = stepQuizCodeDetailsAdapter
            isNestedScrollingEnabled = false

            val divider = MaterialDividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                setDividerThicknessResource(context, R.dimen.divider_vertical_size)
                setDividerColorResource(context, org.hyperskill.app.R.color.color_on_surface_alpha_9)
                isLastItemDecorated = false
            }
            addItemDecoration(divider)
        }

        if (isCollapsible) {
            stepQuizCodeDetails.setOnClickListener {
                stepQuizCodeDetailsArrow.changeState()
                if (stepQuizCodeDetailsArrow.isExpanded()) {
                    detailsCard.expand()
                } else {
                    detailsCard.collapse()
                }
                onDetailsIsExpandedStateChanged()
            }
        } else {
            stepQuizCodeDetailsArrow.isVisible = false
            detailsCard.isVisible = true
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