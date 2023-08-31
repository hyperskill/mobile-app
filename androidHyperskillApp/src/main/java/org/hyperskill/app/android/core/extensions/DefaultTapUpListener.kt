package org.hyperskill.app.android.core.extensions

import android.view.GestureDetector
import android.view.MotionEvent

open class DefaultTapUpListener : GestureDetector.OnGestureListener {
    override fun onDown(e: MotionEvent): Boolean = false

    override fun onShowPress(e: MotionEvent) {
        // no op
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean =
        false

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean =
        false

    override fun onLongPress(e: MotionEvent) {
        // no op
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean =
        false

}

