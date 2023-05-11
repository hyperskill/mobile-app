package org.hyperskill.app.android.study_plan.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStudyPlanSectionBinding
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class StudyPlanSectionAdapterDelegate(
    private val onNewMessage: (StudyPlanWidgetFeature.Message) -> Unit
) : AdapterDelegate<StudyPlanRecyclerItem, DelegateViewHolder<StudyPlanRecyclerItem>>() {

    override fun isForViewType(position: Int, data: StudyPlanRecyclerItem): Boolean =
        data is StudyPlanRecyclerItem.Section

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<StudyPlanRecyclerItem> =
        ViewHolder(createView(parent, R.layout.item_study_plan_section))

    inner class ViewHolder(root: View) : DelegateViewHolder<StudyPlanRecyclerItem>(root) {
        val binding = ItemStudyPlanSectionBinding.bind(itemView)

        private val topArrow = ContextCompat.getDrawable(context, R.drawable.ic_section_arrow_top)
        private val bottomArrow = ContextCompat.getDrawable(context, R.drawable.ic_section_arrow_bottom)

        init {
            itemView.setOnClickListener {
                val section =
                    itemData as? StudyPlanRecyclerItem.Section ?: return@setOnClickListener
                onNewMessage(StudyPlanWidgetFeature.Message.SectionClicked(sectionId = section.id))
            }
        }

        override fun onBind(data: StudyPlanRecyclerItem) {
            data as StudyPlanRecyclerItem.Section

            binding.sectionTitle.text = data.title
            binding.sectionTitle.setTextColor(data.titleTextColor)

            with(binding.sectionArrow) {
                if (data.isExpanded && drawable != bottomArrow) {
                    setImageDrawable(bottomArrow)
                }
                if (!data.isExpanded && drawable != topArrow) {
                    setImageDrawable(topArrow)
                }
            }

            binding.sectionSubtitle.isVisible = data.subtitle != null
            if (data.subtitle != null) {
                binding.sectionSubtitle.text = data.subtitle
            }

            binding.sectionNumbers.isVisible =
                data.formattedTopicsCount != null || data.formattedTimeToComplete != null

            binding.sectionTopicsCount.isVisible = data.formattedTopicsCount != null
            if (data.formattedTopicsCount != null) {
                binding.sectionTopicsCount.text = data.formattedTopicsCount
            }

            binding.sectionTimeToFinish.isVisible = data.formattedTimeToComplete != null
            if (data.formattedTimeToComplete != null) {
                binding.sectionTimeToFinish.text = data.formattedTimeToComplete
            }

            binding.sectionBadgeTextView.isVisible = data.isCurrentBadgeShown
        }
    }
}