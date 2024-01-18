package org.hyperskill.app.core.injection

import android.app.Activity
import java.lang.ref.WeakReference

interface ActivityProvider {
    val activityRef: WeakReference<Activity>?
}