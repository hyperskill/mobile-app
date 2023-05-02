package org.hyperskill.app.android.study_plan.delegate

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.databinding.ItemStudyPlanActivitiesErrorBinding
import org.hyperskill.app.android.study_plan.adapter.StudyPlanActivityAdapterDelegate
import org.hyperskill.app.android.study_plan.adapter.StudyPlanItemAnimator
import org.hyperskill.app.android.study_plan.adapter.StudyPlanSectionAdapterDelegate
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate

class StudyPlanWidgetDelegate(
    private val context: Context,
    private val onNewMessage: (StudyPlanWidgetFeature.Message) -> Unit
) {

    companion object {
        private const val SECTIONS_LOADING_ITEMS_COUNT = 4
        private const val ACTIVITIES_LOADING_ITEMS_COUNT = 3
    }

    private val studyPlanAdapter = DefaultDelegateAdapter<StudyPlanRecyclerItem>().apply {
        addDelegate(StudyPlanSectionAdapterDelegate(onNewMessage))
        addDelegate(StudyPlanActivityAdapterDelegate(onNewMessage))
        addDelegate(sectionsLoadingAdapterDelegate())
        addDelegate(activitiesLoadingAdapterDelegate())
        addDelegate(activitiesErrorAdapterDelegate(onNewMessage))
    }

    @ColorInt private val inactiveSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_60)

    @ColorInt private val activeSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface)

    @ColorInt private val activeActivityTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_87)

    @ColorInt private val inactiveActivityTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_60)

    private val sectionTopMargin =
        context.resources.getDimensionPixelOffset(R.dimen.study_plan_section_top_margin)
    private val activityTopMargin =
        context.resources.getDimensionPixelOffset(R.dimen.study_plan_activity_top_margin)

    private val lockIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_activity_locked)
    private val activeIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_home_screen_arrow_button)
    private val skippedIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_topic_skipped)
    private val completedIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_topic_completed)

    private var studyPlanViewStateDelegate: ViewStateDelegate<StudyPlanWidgetViewState>? = null

    private val sectionsLoadingItems: List<StudyPlanRecyclerItem.SectionLoading> =
        List(SECTIONS_LOADING_ITEMS_COUNT) {
            StudyPlanRecyclerItem.SectionLoading
        }

    fun setup(recyclerView: RecyclerView, errorViewBinding: ErrorNoConnectionWithButtonBinding) {
        studyPlanViewStateDelegate = ViewStateDelegate<StudyPlanWidgetViewState>().apply {
            addState<StudyPlanWidgetViewState.Idle>()
            addState<StudyPlanWidgetViewState.Loading>(recyclerView)
            addState<StudyPlanWidgetViewState.Content>(recyclerView)
            addState<StudyPlanWidgetViewState.Error>(errorViewBinding.root)
        }

        errorViewBinding.tryAgain.setOnClickListener {
            onNewMessage(StudyPlanWidgetFeature.Message.RetryContentLoading)
        }

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = studyPlanAdapter
        setupDecorations(context, recyclerView)
        recyclerView.itemAnimator = StudyPlanItemAnimator()
    }

    private fun setupDecorations(context: Context, recyclerView: RecyclerView) {
        val horizontalMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_horizontal_margin)
        val topMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_top_margin)
        val bottomMargin = context.resources.getDimensionPixelOffset(R.dimen.study_plan_bottom_margin)
        recyclerView.itemDecoration { position, rect, state ->
            rect.left = horizontalMargin
            rect.right = horizontalMargin
            rect.top = if (position == 0) {
                topMargin
            } else {
                val item = studyPlanAdapter.items.getOrNull(position)
                if (item != null) {
                    getTopMarginFor(item)
                } else {
                    0
                }
            }
            if (position == state.itemCount - 1) {
                rect.bottom = bottomMargin
            }
        }
    }

    private fun getTopMarginFor(item: StudyPlanRecyclerItem): Int =
        when (item) {
            StudyPlanRecyclerItem.SectionLoading,
            is StudyPlanRecyclerItem.Section -> sectionTopMargin
            is StudyPlanRecyclerItem.ActivityLoading,
            is StudyPlanRecyclerItem.Activity,
            is StudyPlanRecyclerItem.ActivitiesError -> activityTopMargin
        }

    fun cleanup() {
        studyPlanViewStateDelegate = null
    }

    fun render(state: StudyPlanWidgetViewState) {
        studyPlanViewStateDelegate?.switchState(state)
        when (state) {
            StudyPlanWidgetViewState.Loading -> {
                studyPlanAdapter.items = sectionsLoadingItems
            }
            is StudyPlanWidgetViewState.Content -> {
                studyPlanAdapter.items = mapContentToRecyclerItems(state)
            }
            else -> {
                // no op
            }
        }
    }

    private fun sectionsLoadingAdapterDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.SectionLoading>(
            R.layout.item_study_plan_section_loading
        )

    private fun activitiesLoadingAdapterDelegate() =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.ActivityLoading>(
            R.layout.item_study_plan_activities_loading
        )

    private fun activitiesErrorAdapterDelegate(
        onNewMessage: (StudyPlanWidgetFeature.Message) -> Unit
    ) =
        adapterDelegate<StudyPlanRecyclerItem, StudyPlanRecyclerItem.ActivitiesError>(
            R.layout.item_study_plan_activities_error
        ) {
            val viewBinding = ItemStudyPlanActivitiesErrorBinding.bind(itemView)
            viewBinding.activitiesReloadButton.setOnClickListener {
                item?.sectionId?.let { sectionId ->
                    onNewMessage(StudyPlanWidgetFeature.Message.RetryActivitiesLoading(sectionId))
                }
            }
        }

    private fun mapContentToRecyclerItems(
        studyPlanContent: StudyPlanWidgetViewState.Content
    ): List<StudyPlanRecyclerItem> =
        studyPlanContent.sections.flatMapIndexed { index, section ->
            buildList {
                add(mapSectionToRecyclerItem(index, section))
                when (val sectionContent = section.content) {
                    StudyPlanWidgetViewState.SectionContent.Collapsed -> {
                        // no op
                    }
                    StudyPlanWidgetViewState.SectionContent.Loading -> {
                        addAll(
                            List(ACTIVITIES_LOADING_ITEMS_COUNT) { index ->
                                StudyPlanRecyclerItem.ActivityLoading(section.id, index)
                            }
                        )
                    }
                    is StudyPlanWidgetViewState.SectionContent.Content -> {
                        addAll(mapSectionContentToActivityItems(sectionContent))
                    }
                    StudyPlanWidgetViewState.SectionContent.Error -> {
                        add(StudyPlanRecyclerItem.ActivitiesError(section.id))
                    }
                }
            }
        }

    private fun mapSectionToRecyclerItem(
        index: Int,
        section: StudyPlanWidgetViewState.Section
    ): StudyPlanRecyclerItem.Section =
        StudyPlanRecyclerItem.Section(
            id = section.id,
            title = section.title,
            titleTextColor = if (index == 0) {
                activeSectionTextColor
            } else {
                inactiveSectionTextColor
            },
            subtitle = section.subtitle,
            formattedTopicsCount = section.formattedTopicsCount,
            formattedTimeToComplete = section.formattedTimeToComplete,
            isExpanded = section.content !is StudyPlanWidgetViewState.SectionContent.Collapsed
        )

    private fun mapSectionContentToActivityItems(
        content: StudyPlanWidgetViewState.SectionContent.Content
    ): List<StudyPlanRecyclerItem.Activity> =
        content.sectionItems.map { item ->
            StudyPlanRecyclerItem.Activity(
                id = item.id,
                title = item.title,
                titleTextColor = if (item.state == StudyPlanWidgetViewState.SectionItemState.NEXT) {
                    activeActivityTextColor
                } else {
                    inactiveActivityTextColor
                },
                progress = item.progress,
                formattedProgress = item.formattedProgress,
                endIcon = when (item.state) {
                    StudyPlanWidgetViewState.SectionItemState.IDLE -> null
                    StudyPlanWidgetViewState.SectionItemState.NEXT -> activeIcon
                    StudyPlanWidgetViewState.SectionItemState.LOCKED -> lockIcon
                    StudyPlanWidgetViewState.SectionItemState.SKIPPED -> skippedIcon
                    StudyPlanWidgetViewState.SectionItemState.COMPLETED -> completedIcon
                },
                isClickable = item.isClickable,
                isIdeRequired = item.isIdeRequired
            )
        }
}