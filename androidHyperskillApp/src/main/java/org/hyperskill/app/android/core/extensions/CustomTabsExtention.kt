package org.hyperskill.app.android.core.extensions

import android.content.Context
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import org.hyperskill.app.R

fun CustomTabsIntent.Builder.setHyperskillColors(context: Context): CustomTabsIntent.Builder =
    setDefaultColorSchemeParams(
        CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.color_primary_variant))
            .build()
    )