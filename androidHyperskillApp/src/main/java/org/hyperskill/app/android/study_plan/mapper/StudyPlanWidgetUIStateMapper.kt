package org.hyperskill.app.android.study_plan.mapper

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import org.hyperskill.app.android.R
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState.SectionContentPageLoadingState

class StudyPlanWidgetUIStateMapper(context: Context) {

    companion object {
        private const val SECTIONS_LOADING_ITEMS_COUNT = 4
        private const val ACTIVITIES_LOADING_ITEMS_COUNT = 3
    }

    private val sectionsLoadingItems: List<StudyPlanRecyclerItem.SectionLoading> =
        List(SECTIONS_LOADING_ITEMS_COUNT) { index ->
            StudyPlanRecyclerItem.SectionLoading(index)
        }

    @ColorInt
    private val inactiveSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_60)

    @ColorInt
    private val activeSectionTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface)

    @ColorInt
    private val activeActivityTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_87)

    @ColorInt
    private val inactiveActivityTextColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_60)

    private val activeIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_home_screen_arrow_button)
    private val skippedIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_topic_skipped)
    private val completedIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_topic_completed)
    private val lockedIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_activity_locked)

    fun map(state: StudyPlanWidgetViewState): List<StudyPlanRecyclerItem> =
        when (state) {
            StudyPlanWidgetViewState.Loading -> sectionsLoadingItems
            is StudyPlanWidgetViewState.Content -> mapContentToRecyclerItems(state)
            else -> emptyList()
        }

    private fun mapContentToRecyclerItems(
        studyPlanContent: StudyPlanWidgetViewState.Content
    ): List<StudyPlanRecyclerItem> =
        buildList {
            if (studyPlanContent.isPaywallBannerShown) {
                add(StudyPlanRecyclerItem.PaywallBanner)
            }
            studyPlanContent.sections.forEachIndexed { sectionIndex, section ->
                add(mapSectionToRecyclerItem(sectionIndex, section))
                when (val sectionContent = section.content) {
                    StudyPlanWidgetViewState.SectionContent.Collapsed -> {
                        // no op
                    }
                    StudyPlanWidgetViewState.SectionContent.Loading -> {
                        addAll(getActivitiesLoadingItems(section.id))
                    }
                    is StudyPlanWidgetViewState.SectionContent.Content -> {
                        when (sectionContent.completedPageLoadingState) {
                            SectionContentPageLoadingState.HIDDEN -> {
                                // no op
                            }
                            SectionContentPageLoadingState.LOAD_MORE -> {
                                add(StudyPlanRecyclerItem.ExpandCompletedActivitiesButton(section.id))
                            }
                            SectionContentPageLoadingState.LOADING -> {
                                addAll(getActivitiesLoadingItems(section.id))
                            }
                        }
                        addAll(mapSectionItemsToActivityItems(section.id, sectionContent.sectionItems))
                        when (sectionContent.nextPageLoadingState) {
                            SectionContentPageLoadingState.HIDDEN -> {
                                // no op
                            }
                            SectionContentPageLoadingState.LOAD_MORE -> {
                                add(StudyPlanRecyclerItem.LoadAllTopicsButton(section.id))
                            }
                            SectionContentPageLoadingState.LOADING -> {
                                addAll(getActivitiesLoadingItems(section.id))
                            }
                        }
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
            isExpanded = section.content !is StudyPlanWidgetViewState.SectionContent.Collapsed,
            isCurrentBadgeShown = section.isCurrentBadgeShown
        )

    private fun mapSectionItemsToActivityItems(
        sectionId: Long,
        sectionItems: List<StudyPlanWidgetViewState.SectionItem>
    ): List<StudyPlanRecyclerItem.Activity> =
        sectionItems.map { item ->
            StudyPlanRecyclerItem.Activity(
                id = item.id,
                sectionId = sectionId,
                title = item.title,
                subtitle = item.subtitle,
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
                    StudyPlanWidgetViewState.SectionItemState.SKIPPED -> skippedIcon
                    StudyPlanWidgetViewState.SectionItemState.COMPLETED -> completedIcon
                    StudyPlanWidgetViewState.SectionItemState.LOCKED -> lockedIcon
                },
                isIdeRequired = item.isIdeRequired
            )
        }

    private fun getActivitiesLoadingItems(sectionId: Long) =
        List(ACTIVITIES_LOADING_ITEMS_COUNT) { index ->
            StudyPlanRecyclerItem.ActivityLoading(sectionId, index)
        }
}