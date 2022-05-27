package org.hyperskill.app.core.injection

import android.content.Context

interface AndroidAppComponent : AppGraph {
    val context: Context
}