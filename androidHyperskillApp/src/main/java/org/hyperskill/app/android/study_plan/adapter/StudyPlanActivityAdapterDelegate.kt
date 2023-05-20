package org.hyperskill.app.android.study_plan.adapter

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStudyPlanActivityBinding
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class StudyPlanActivityAdapterDelegate(
    private val onNewMessage: (StudyPlanWidgetFeature.Message) -> Unit
) :
    AdapterDelegate<StudyPlanRecyclerItem, DelegateViewHolder<StudyPlanRecyclerItem>>() {

    override fun isForViewType(position: Int, data: StudyPlanRecyclerItem): Boolean =
        data is StudyPlanRecyclerItem.Activity

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<StudyPlanRecyclerItem> =
        ViewHolder(createView(parent, R.layout.item_study_plan_activity))

    inner class ViewHolder(root: View) : DelegateViewHolder<StudyPlanRecyclerItem>(root) {

        private val binding: ItemStudyPlanActivityBinding = ItemStudyPlanActivityBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                val activityId = (itemData as? StudyPlanRecyclerItem.Activity)?.id
                if (activityId != null) {
                    onNewMessage(StudyPlanWidgetFeature.Message.ActivityClicked(activityId))
                }
            }
        }

        override fun onBind(data: StudyPlanRecyclerItem) {
            data as StudyPlanRecyclerItem.Activity

            with(binding.activityTitle) {
                text = data.title
                setTextColor(data.titleTextColor)
            }

            with(binding.activitySubtitle) {
                isVisible = data.subtitle != null
                text = data.subtitle
                setTextColor(data.titleTextColor)
            }

            val isProgressVisible =
                data.progress != null && data.progress > 0f && data.formattedProgress != null

            with(binding.activityCompletenessView) {
                isVisible = isProgressVisible
                if (data.progress != null) {
                    updateLayoutParams<ConstraintLayout.LayoutParams> {
                        matchConstraintPercentWidth = data.progress
                    }
                }
            }

            binding.activityBadges.isVisible = data.isIdeRequired || isProgressVisible

            with(binding.activityCompletenessTextView) {
                isVisible = isProgressVisible
                if (data.formattedProgress != null) {
                    text = data.formattedProgress
                }
            }

            binding.activityIdeRequiredTextView.isVisible = data.isIdeRequired

            binding.activityEndIcon.setImageDrawable(data.endIcon)

            itemView.isClickable = data.isClickable
        }
    }
}