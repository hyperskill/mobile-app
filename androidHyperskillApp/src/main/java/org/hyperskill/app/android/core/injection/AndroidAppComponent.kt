package org.hyperskill.app.android.core.injection

import android.content.Context
import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.notification.injection.PlatformNotificationComponent
import org.hyperskill.app.core.injection.CommonAndroidAppGraph

interface AndroidAppComponent : CommonAndroidAppGraph {
    val context: Context
    val platformNotificationComponent: PlatformNotificationComponent
    val imageLoadingComponent: ImageLoadingComponent
    val navigationComponent: NavigationComponent

    fun buildPlatformLatexComponent(): PlatformLatexComponent
    fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent
}