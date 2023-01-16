package org.hyperskill.app.android.image_loading.injection

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder

class ImageLoadingComponentImpl(context: Context) : ImageLoadingComponent {
    override val imageLoader: ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
}