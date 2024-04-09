package org.hyperskill.app.android.core.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import co.touchlab.kermit.Logger
import org.hyperskill.app.R

fun CustomTabsIntent.Builder.setHyperskillColors(context: Context): CustomTabsIntent.Builder =
    setDefaultColorSchemeParams(
        CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.color_primary_variant))
            .build()
    )

fun Fragment.launchUrlInCustomTabs(
    url: String,
    logger: Logger
) {
    launchUrlInCustomTabs(url) { e ->
        logger.e(e) {
            "Unable to launch url in custom tabs nor in browser. url=$url"
        }
        Toast.makeText(
            requireContext(),
            getString(R.string.external_link_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Fragment.launchUrlInCustomTabs(
    url: String,
    onError: (e: Throwable) -> Unit
) {
    CustomTabsIntent.Builder()
        .setHyperskillColors(requireContext())
        .build()
        .launchUrlSafe(
            requireActivity(),
            Uri.parse(url),
            onError
        )
}

fun CustomTabsIntent.launchUrlSafe(
    activity: Activity,
    url: Uri,
    onError: (e: Throwable) -> Unit
) {
    try {
        launchUrl(activity, url)
    // ActivityNotFoundException means there is no browser on the device
    } catch (e: ActivityNotFoundException) {
        onError(e)
    }
}

@Deprecated(
    message = "Don't call launchUrl directly. Use launchUrlSafe instead.",
    replaceWith = ReplaceWith(
        expression = "launchUrlSafe",
        imports = arrayOf("org.hyperskill.app.android.core.extensions.launchUrlSafe")
    ),
    level = DeprecationLevel.ERROR
)
@Suppress("EXTENSION_SHADOWED_BY_MEMBER", "UnusedParameter")
fun CustomTabsIntent.launchUrl(context: Context, url: Uri): Nothing =
    error("Don't call launchUrl directly. Use launchUrlSafe instead.")