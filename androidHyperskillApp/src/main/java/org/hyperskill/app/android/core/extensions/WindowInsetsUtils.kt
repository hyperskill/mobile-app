package org.hyperskill.app.android.core.extensions

import android.view.View
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.OnApplyInsetsListener

fun View.doOnApplyWindowInsets(
    @Insetter.ConsumeOptions consume: Int = Insetter.CONSUME_AUTO,
    onApplyInsetsListener: OnApplyInsetsListener
) {
    Insetter
        .builder()
        .setOnApplyInsetsListener(onApplyInsetsListener)
        .consume(consume)
        .applyToView(this)
}