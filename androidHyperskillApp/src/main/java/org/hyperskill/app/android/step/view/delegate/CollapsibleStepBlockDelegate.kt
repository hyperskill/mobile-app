package org.hyperskill.app.android.step.view.delegate

import android.view.View
import org.hyperskill.app.android.ui.custom.ArrowImageView
import org.hyperskill.app.android.view.base.ui.extension.collapse
import org.hyperskill.app.android.view.base.ui.extension.expand

object CollapsibleStepBlockDelegate {
    fun setupCollapsibleBlock(
        arrowView: ArrowImageView,
        headerView: View,
        contentView: View,
        onContentExpandChanged: (Boolean) -> Unit = {}
    ) {
        headerView.setOnClickListener {
            arrowView.changeState()
            if (arrowView.isExpanded()) {
                contentView.expand()
            } else {
                contentView.collapse()
            }
            onContentExpandChanged(arrowView.isExpanded())
        }
    }
}