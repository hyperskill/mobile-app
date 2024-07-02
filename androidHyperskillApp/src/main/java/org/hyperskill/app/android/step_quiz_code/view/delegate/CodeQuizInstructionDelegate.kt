package org.hyperskill.app.android.step_quiz_code.view.delegate

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeDetailsBinding
import org.hyperskill.app.android.step.view.delegate.CollapsibleStepBlockDelegate
import org.hyperskill.app.android.step_quiz_code.view.adapter.CodeDetailSampleAdapterDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.CodeDetail
import org.hyperskill.app.code.domain.model.ProgrammingLanguage
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class CodeQuizInstructionDelegate(
    private val binding: LayoutStepQuizCodeDetailsBinding,
    isCollapsible: Boolean,
    onDetailsIsExpandedStateChanged: (Boolean) -> Unit
) {
    private val stepQuizCodeDetailsAdapter = DefaultDelegateAdapter<CodeDetail>()

    init {
        stepQuizCodeDetailsAdapter += CodeDetailSampleAdapterDelegate()

        with(binding.stepQuizCodeDetailsRecycler) {
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
            CollapsibleStepBlockDelegate.setupCollapsibleBlock(
                arrowView = binding.stepQuizCodeDetailsArrow,
                headerView = binding.stepQuizCodeDetailsFrameLayout,
                contentView = binding.stepQuizCodeDetailsMaterialCard,
                onContentExpandChanged = onDetailsIsExpandedStateChanged
            )
        } else {
            binding.stepQuizCodeDetailsArrow.isVisible = false
            binding.stepQuizCodeDetailsMaterialCard.isVisible = true
        }
    }

    fun setCodeDetailsData(details: List<CodeDetail>, lang: String?) {
        if (lang == ProgrammingLanguage.SQL.languageName) {
            binding.root.isVisible = false
        } else {
            stepQuizCodeDetailsAdapter.items = details
            binding.root.isVisible = details.isNotEmpty()
        }
    }
}