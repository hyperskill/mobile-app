package org.hyperskill.app.play_services.data

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import org.hyperskill.app.play_services.domain.PlayServicesChecker
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.breadcrumb.HyperskillSentryBreadcrumbBuilder

class PlayServicesCheckerImpl(
    private val context: Context,
    private val sentryInteractor: SentryInteractor
) : PlayServicesChecker {
    override fun arePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
        val isAvailable = if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                // do not show Google Services dialog
                // it is resolvable, but we do not want push user for updating services
                sentryInteractor.addBreadcrumb(
                    HyperskillSentryBreadcrumbBuilder.buildGoogleServicesTooOld()
                )
            }
            false
        } else {
            true
        }
        return isAvailable
    }
}