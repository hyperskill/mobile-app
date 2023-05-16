package org.hyperskill.app.android.projects_selection.delegate

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.size.Scale
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.databinding.ItemProjectBlockHeaderBinding
import org.hyperskill.app.android.databinding.ItemProjectSelectionHeaderBinding
import org.hyperskill.app.android.projects_selection.adapter.ProjectAdapterDelegate
import org.hyperskill.app.android.projects_selection.model.ProjectSelectionRecyclerItem
import org.hyperskill.app.project_selection.presentation.ProjectSelectionListFeature
import org.hyperskill.app.projects.domain.model.ProjectLevel
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class ProjectSelectionListDelegate(
    context: Context,
    recyclerView: RecyclerView,
    private val imageLoader: ImageLoader,
    onNewMessage: (ProjectSelectionListFeature.Message) -> Unit
) {

    private val projectSelectionAdapter =
        DefaultDelegateAdapter<ProjectSelectionRecyclerItem>().apply {
            addDelegate(
                ProjectAdapterDelegate { projectId: Long ->
                    onNewMessage(ProjectSelectionListFeature.Message.ProjectClicked(projectId))
                }
            )
            addDelegate(sectionTitleAdapterDelegate())
            addDelegate(headerAdapterDelegate())
        }

    private val firstItemTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_first_item_top_margin
        )

    private val lastItemBottomMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_last_item_bottom_margin
        )

    private val sectionTitleTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_section_title_top_margin
        )

    private val sectionFirstItemTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_section_first_item_top_margin
        )

    private val sectionItemTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_section_item_top_margin
        )

    private val horizontalMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_horizontal_margin
        )

    private val recommendedProjectsTitle =
        ProjectSelectionRecyclerItem.SectionTitle(
            title = context.getString(
                org.hyperskill.app.R.string.projects_list_recommended_projects_title
            ),
            subtitle = null
        )

    private val levelTitleByLevelMap: Map<ProjectLevel, String> =
        ProjectLevel.values().associateWith { level ->
            when (level) {
                ProjectLevel.EASY ->
                    org.hyperskill.app.R.string.projects_list_easy_category_title
                ProjectLevel.MEDIUM ->
                    org.hyperskill.app.R.string.projects_list_medium_category_title
                ProjectLevel.HARD ->
                    org.hyperskill.app.R.string.projects_list_hard_category_title
                ProjectLevel.NIGHTMARE ->
                    org.hyperskill.app.R.string.projects_list_nightmare_category_title
            }.let(context::getString)
        }

    private val levelSubtitleByLevelMap: Map<ProjectLevel, String> =
        ProjectLevel.values().associateWith { level ->
            when (level) {
                ProjectLevel.EASY ->
                    org.hyperskill.app.R.string.projects_list_easy_category_description
                ProjectLevel.MEDIUM ->
                    org.hyperskill.app.R.string.projects_list_medium_category_description
                ProjectLevel.HARD ->
                    org.hyperskill.app.R.string.projects_list_hard_category_description
                ProjectLevel.NIGHTMARE ->
                    org.hyperskill.app.R.string.projects_list_nightmare_category_description
            }.let(context::getString)
        }

    @ColorInt
    private val selectedStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_overlay_blue)

    @ColorInt
    private val notSelectedStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_9)

    private val levelIconByLevelMap: Map<ProjectLevel, Drawable?> =
        ProjectLevel.values().associateWith { level ->
            when (level) {
                ProjectLevel.EASY -> R.drawable.ic_level_easy
                ProjectLevel.MEDIUM -> R.drawable.ic_level_medium
                ProjectLevel.HARD -> R.drawable.ic_level_hard
                ProjectLevel.NIGHTMARE -> R.drawable.ic_level_challenging
            }.let { res ->
                ContextCompat.getDrawable(context, res)
            }
        }

    init {
        with(recyclerView) {
            this.adapter = projectSelectionAdapter
            layoutManager = LinearLayoutManager(context)
            setupDecorations(recyclerView, projectSelectionAdapter)
        }
    }

    private fun setupDecorations(
        recyclerView: RecyclerView,
        projectSelectionAdapter: DefaultDelegateAdapter<ProjectSelectionRecyclerItem>
    ) {
        recyclerView.itemDecoration { position, rect, _ ->
            rect.left = horizontalMargin
            rect.right = horizontalMargin
            if (position == projectSelectionAdapter.itemCount - 1) {
                rect.bottom = lastItemBottomMargin
            }
            rect.top = when (projectSelectionAdapter.items.getOrNull(position)) {
                is ProjectSelectionRecyclerItem.Header -> 0
                is ProjectSelectionRecyclerItem.SectionTitle -> sectionTitleTopMargin
                is ProjectSelectionRecyclerItem.Project -> {
                    when (projectSelectionAdapter.items.getOrNull(position - 1)) {
                        is ProjectSelectionRecyclerItem.Header -> firstItemTopMargin
                        is ProjectSelectionRecyclerItem.SectionTitle ->
                            sectionFirstItemTopMargin
                        is ProjectSelectionRecyclerItem.Project ->
                            sectionItemTopMargin
                        null -> 0
                    }
                }
                null -> 0
            }
        }
    }

    fun render(state: ProjectSelectionListFeature.ViewState.Content) {
        projectSelectionAdapter.items = buildList {
            add(
                ProjectSelectionRecyclerItem.Header(
                    title = state.formattedTitle,
                    trackIcon = state.trackIcon
                )
            )
            state.selectedProject?.let {
                add(mapViewStateProject(it, isSelected = true))
            }
            if (state.recommendedProjects.isNotEmpty()) {
                add(recommendedProjectsTitle)
            }
            addAll(state.recommendedProjects.map(::mapViewStateProject))
            ProjectLevel.values().forEach { level ->
                val levelProjects = state.projectsByLevel[level]
                if (!levelProjects.isNullOrEmpty()) {
                    add(
                        ProjectSelectionRecyclerItem.SectionTitle(
                            title = levelTitleByLevelMap[level] ?: "",
                            subtitle = levelSubtitleByLevelMap[level]
                        )
                    )
                    addAll(levelProjects.map(::mapViewStateProject))
                }
            }
        }
    }

    private fun sectionTitleAdapterDelegate() =
        adapterDelegate<ProjectSelectionRecyclerItem, ProjectSelectionRecyclerItem.SectionTitle>(
            R.layout.item_project_block_header
        ) {
            val binding = ItemProjectBlockHeaderBinding.bind(itemView)
            onBind {
                with(binding) {
                    projectBlockTitle.setTextIfChanged(it.title)
                    projectBlockSubtitle.isVisible = it.subtitle != null
                    if (it.subtitle != null) {
                        projectBlockSubtitle.setTextIfChanged(it.subtitle)
                    }
                }
            }
        }

    private fun headerAdapterDelegate() =
        adapterDelegate<ProjectSelectionRecyclerItem, ProjectSelectionRecyclerItem.Header>(
            R.layout.item_project_selection_header
        ) {
            val binding = ItemProjectSelectionHeaderBinding.bind(itemView)
            onBind {
                with(binding) {
                    projectSelectionListTrackName.setTextIfChanged(it.title)
                    projectSelectionTrackIconImageView.load(it.trackIcon, imageLoader) {
                        scale(Scale.FILL)
                    }
                }
            }
        }

    private fun mapViewStateProject(
        project: ProjectSelectionListFeature.ProjectListItem,
        isSelected: Boolean = false
    ) =
        ProjectSelectionRecyclerItem.Project(
            id = project.id,
            title = project.title,
            formattedTimeToComplete = project.formattedTimeToComplete,
            isSelected = isSelected,
            isGraduate = project.isGraduate,
            isBestRated = project.isBestRated,
            isIdeRequired = project.isIdeRequired,
            isFastestToComplete = project.isFastestToComplete,
            isCompleted = project.isCompleted,
            formattedRating = project.averageRating.toString(),
            levelText = project.level?.let(levelTitleByLevelMap::get),
            levelIcon = levelIconByLevelMap[project.level],
            strokeColor = if (isSelected) selectedStrokeColor else notSelectedStrokeColor
        )
}