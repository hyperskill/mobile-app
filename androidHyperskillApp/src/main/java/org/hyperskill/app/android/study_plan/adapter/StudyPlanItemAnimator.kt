package org.hyperskill.app.android.study_plan.adapter

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class StudyPlanItemAnimator : DefaultItemAnimator() {

    init {
        supportsChangeAnimations = false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchRemoveFinished(holder)
        return false
    }
}