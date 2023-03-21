package org.hyperskill.app.android.study_plan.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.StudyPlanSectionItemBinding
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class StudyPlanSectionAdapterDelegate :
    AdapterDelegate<StudyPlanRecyclerItem, DelegateViewHolder<StudyPlanRecyclerItem>>() {

    override fun isForViewType(position: Int, data: StudyPlanRecyclerItem): Boolean =
        data is StudyPlanRecyclerItem.Section

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<StudyPlanRecyclerItem> =
        ViewHolder(createView(parent, R.layout.study_plan_section_item))

    inner class ViewHolder(root: View) : DelegateViewHolder<StudyPlanRecyclerItem>(root) {
        val binding = StudyPlanSectionItemBinding.bind(itemView)

        override fun onBind(data: StudyPlanRecyclerItem) {
            data as StudyPlanRecyclerItem.Section

            binding.sectionTitle.text = data.title
            binding.sectionTitle.setTextColor(data.titleTextColor)

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
        }
    }
}