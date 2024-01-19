package org.hyperskill.app.android.core.injection

import org.hyperskill.app.android.code.injection.PlatformCodeEditorComponent
import org.hyperskill.app.android.image_loading.injection.ImageLoadingComponent
import org.hyperskill.app.android.latex.injection.PlatformLatexComponent
import org.hyperskill.app.android.main.injection.NavigationComponent
import org.hyperskill.app.android.notification.local.injection.PlatformLocalNotificationComponent
import org.hyperskill.app.android.notification.remote.injection.AndroidPlatformPushNotificationComponent
import org.hyperskill.app.core.injection.CommonAndroidAppGraph

interface AndroidAppComponent : CommonAndroidAppGraph {
    val platformLocalNotificationComponent: PlatformLocalNotificationComponent
    val imageLoadingComponent: ImageLoadingComponent
    val navigationComponent: NavigationComponent

    fun buildPlatformLatexComponent(): PlatformLatexComponent
    fun buildPlatformCodeEditorComponent(): PlatformCodeEditorComponent

    fun buildPlatformPushNotificationsComponent(): AndroidPlatformPushNotificationComponent
}