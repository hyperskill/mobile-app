package org.hyperskill.app.android.core.extensions

import android.graphics.drawable.LayerDrawable

class LayerListDrawableDelegate(
    private val layerIds: List<Int>,
    private val layers: LayerDrawable
) {
    fun showLayer(visibleLayerId: Int) {
        for (layerId in layerIds) {
            val layer = layers.findDrawableByLayerId(layerId).mutate()
            layer.alpha =
                if (layerId == visibleLayerId) 255 else 0
            layers.setDrawableByLayerId(layerId, layer)
            layers.invalidateSelf()
        }
    }
}