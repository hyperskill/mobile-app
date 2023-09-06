package org.hyperskill.app.android.core.extensions

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent

class SingleTapGestureDetector(
    context: Context,
    onSingleTapUp: (e: MotionEvent) -> Boolean
) : GestureDetector(
    context,
    object : DefaultTapUpListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean =
            onSingleTapUp(e)
    }
)