package org.hyperskill.app.android.study_plan.adapter

import android.view.ViewGroup
import org.hyperskill.app.android.R
import org.hyperskill.app.android.study_plan.model.StudyPlanRecyclerItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class ActivityLoadingAdapterDelegate : AdapterDelegate<StudyPlanRecyclerItem, DelegateViewHolder<StudyPlanRecyclerItem>>() {
    override fun isForViewType(position: Int, data: StudyPlanRecyclerItem): Boolean =
        data is StudyPlanRecyclerItem.ActivityLoading

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<StudyPlanRecyclerItem> =
        object : DelegateViewHolder<StudyPlanRecyclerItem>(
            createView(parent, R.layout.item_study_plan_activities_loading)
        ) {}
}