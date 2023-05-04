package org.hyperskill.app.android.core.view.ui

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

fun SwipeRefreshLayout.updateIsRefreshing(isRefreshing: Boolean) {
    if (this.isRefreshing && !isRefreshing) {
        this.isRefreshing = false
    }
}

fun SwipeRefreshLayout.setHyperskillColors() {
    setProgressBackgroundColorSchemeResource(org.hyperskill.app.R.color.color_surface)
    setColorSchemeResources(org.hyperskill.app.R.color.color_primary)
}