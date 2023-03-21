package org.hyperskill.app.android.study_plan.delegate

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.study_plan.adapter.StudyPlanSectionAdapterDelegate
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class StudyPlanWidgetDelegate(private val context: Context) {

    private val studyPlanAdapter = DefaultDelegateAdapter<StudyPlanRecyclerItem>().apply {
        addDelegate(StudyPlanSectionAdapterDelegate())
    }

    @ColorInt private val collapsedSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_60)

    @ColorInt private val expandedSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface)

    fun setup(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = studyPlanAdapter
        setupDecorations(context, recyclerView)
    }

    private fun setupDecorations(context: Context, recyclerView: RecyclerView) {
        val horizontalMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_horizontal_margin)
        val topMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_top_margin)
        val bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_bottom_margin)
        val sectionTopMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_section_top_margin)
        recyclerView.itemDecoration { position, rect, state ->
            rect.left = horizontalMargin
            rect.right = horizontalMargin
            rect.top = when {
                position == 0 -> topMargin
                studyPlanAdapter.items[position] is StudyPlanRecyclerItem.Section -> sectionTopMargin
                else -> 0
            }
            if (position == state.itemCount - 1) {
                rect.bottom = bottomMargin
            }
        }
    }

    fun render(state: StudyPlanWidgetViewState) {
        when (state) {
            StudyPlanWidgetViewState.Idle -> {
                // TODO
            }
            StudyPlanWidgetViewState.Loading -> {
                // TODO
            }
            is StudyPlanWidgetViewState.Content -> {
                studyPlanAdapter.items = state.sections.map { section ->
                    StudyPlanRecyclerItem.Section(
                        id = section.id,
                        title = section.title,
                        titleTextColor = if (section.content is StudyPlanWidgetViewState.SectionContent.Collapsed) {
                            collapsedSectionTextColor
                        } else {
                            expandedSectionTextColor
                        },
                        subtitle = section.subtitle,
                        formattedTopicsCount = section.formattedTopicsCount,
                        formattedTimeToComplete = section.formattedTimeToComplete
                    )
                }
            }
            StudyPlanWidgetViewState.Error -> {
                // TODO
            }
        }
    }
}