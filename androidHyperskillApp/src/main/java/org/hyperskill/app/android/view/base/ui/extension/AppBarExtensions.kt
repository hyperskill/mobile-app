package org.hyperskill.app.android.view.base.ui.extension

import androidx.lifecycle.Lifecycle
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs
import org.hyperskill.app.android.R

enum class  AppBarLayoutState { EXPANDED, COLLAPSED, IDLE }

abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    private var state: AppBarLayoutState = AppBarLayoutState.IDLE

    final override fun onOffsetChanged(layout: AppBarLayout, verticalOffset: Int) {
        val newState = when {
            verticalOffset == 0 -> AppBarLayoutState.EXPANDED
            abs(verticalOffset) >= layout.totalScrollRange -> AppBarLayoutState.COLLAPSED
            else -> AppBarLayoutState.IDLE
        }
        if (state == newState) return
        state = newState
        onStateChanged(layout, state)
    }

    abstract fun onStateChanged(layout: AppBarLayout, state: AppBarLayoutState)
}

/**
 * Set elevation to [R.dimen.appbar_elevation] when [AppBarLayout] is collapsed, otherwise set it to zero.
 * @param [lifecycle] - lifecycle to bound callback to it.
 */
fun AppBarLayout.setElevationOnCollapsed(lifecycle: Lifecycle) {
    addOnOffsetChangedListener(
        object : AppBarStateChangeListener() {
            override fun onStateChanged(layout: AppBarLayout, state: AppBarLayoutState) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    layout.elevation = when (state) {
                        AppBarLayoutState.COLLAPSED -> resources.getDimensionPixelOffset(R.dimen.appbar_elevation).toFloat()
                        else -> 0f
                    }
                }
            }
        }
    )
}