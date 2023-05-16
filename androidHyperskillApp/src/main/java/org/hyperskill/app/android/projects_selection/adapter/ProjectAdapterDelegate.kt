package org.hyperskill.app.android.projects_selection.adapter

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemProjectBinding
import org.hyperskill.app.android.projects_selection.model.ProjectSelectionRecyclerItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class ProjectAdapterDelegate(
    private val onProjectClicked: (Long) -> Unit
) : AdapterDelegate<ProjectSelectionRecyclerItem, DelegateViewHolder<ProjectSelectionRecyclerItem>>() {

    override fun isForViewType(position: Int, data: ProjectSelectionRecyclerItem): Boolean =
        data is ProjectSelectionRecyclerItem.Project

    override fun onCreateViewHolder(
        parent: ViewGroup
    ): ViewHolder =
        ViewHolder(createView(parent, R.layout.item_project))

    inner class ViewHolder(root: View) : DelegateViewHolder<ProjectSelectionRecyclerItem>(root) {

        private val binding: ItemProjectBinding = ItemProjectBinding.bind(itemView)

        private val tagsTopMargin = context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_item_tags_top_margin
        )

        init {
            itemView.setOnClickListener {
                val projectId = (itemData as? ProjectSelectionRecyclerItem.Project)?.id
                if (projectId != null) {
                    onProjectClicked(projectId)
                }
            }
        }

        override fun onBind(data: ProjectSelectionRecyclerItem) {
            data as ProjectSelectionRecyclerItem.Project

            with(binding) {
                root.isClickable = data.isClickable
                root.strokeColor = data.strokeColor

                projectTitle.setTextIfChanged(data.title)
                projectRating.setTextIfChanged(data.formattedRating)

                with(projectDuration) {
                    isVisible = data.formattedTimeToComplete != null
                    if (data.formattedTimeToComplete != null) {
                        setTextIfChanged(data.formattedTimeToComplete)
                    }
                }

                with(projectLevel) {
                    isVisible = data.levelText != null
                    if (data.levelText != null) {
                        setTextIfChanged(data.levelText)
                        setCompoundDrawablesWithIntrinsicBounds(
                            /* left = */ data.levelIcon,
                            /* top = */ null,
                            /* right = */ null,
                            /* bottom = */ null
                        )
                    }
                }

                projectGraduateText.isVisible = data.isGraduate

                projectCompletedTextView.isVisible = false // TODO: add isCompleted flag to data
                projectSelectedTextView.isVisible = data.isSelected
                projectIdeRequiredTextView.isVisible = data.isIdeRequired
                projectBestRatingTextView.isVisible = data.isBestRated
                projectFastestToCompleteTextView.isVisible = data.isFastestToComplete

                val newTagsTopMargin = if (data.areTagsVisible) tagsTopMargin else 0
                if ((projectTags.layoutParams as? MarginLayoutParams)?.topMargin != tagsTopMargin) {
                    projectTags.updateLayoutParams<MarginLayoutParams> {
                        topMargin = newTagsTopMargin
                    }
                }
            }
        }
    }
}