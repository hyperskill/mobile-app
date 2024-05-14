package org.hyperskill.app.analytic.domain.apps_flyer

import android.content.Context
import co.touchlab.kermit.Logger
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEngine
import org.hyperskill.app.config.BuildKonfig

class AndroidAppsFlyerAnalyticEngine(
    logger: Logger,
    private val applicationContext: Context,
    private val isDebugMode: Boolean
) : AppsFlyerAnalyticEngine() {

    private val logger: Logger =
        logger.withTag("AppsFlyerAnalyticEngine")

    @Suppress("MagicNumber")
    fun startup() {
        val appsFlyerLib = AppsFlyerLib.getInstance()
        if (isDebugMode) {
            appsFlyerLib.setDebugLog(true)
            appsFlyerLib.setMinTimeBetweenSessions(0)
        }
        appsFlyerLib.init(BuildKonfig.APPS_FLYER_DEV_KEY, null, applicationContext)
        appsFlyerLib
            .start(
                applicationContext,
                BuildKonfig.APPS_FLYER_DEV_KEY,
                object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        logger.d { "Apps flyer has started successfully" }
                    }
                    override fun onError(errorCode: Int, errorDesc: String) {
                        logger.e { "Apps flyer failed to start. ErrorCode=$errorCode, errorDesc=$errorDesc" }
                    }
                }
            )
    }

    override suspend fun reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Boolean
    ) {
        AppsFlyerLib
            .getInstance()
            .setCustomerUserId(userProperties.userId?.toString())
        AppsFlyerLib
            .getInstance()
            .logEvent(
                applicationContext,
                event.name,
                event.params,
                object : AppsFlyerRequestListener {
                    override fun onSuccess() {
                        logger.d { "Apps flyer successfully logged event ${event.name}" }
                    }
                    override fun onError(errorCode: Int, errorDesc: String) {
                        logger.e {
                            "Apps flyer failed to log event ${event.name}. ErrorCode=$errorCode, errorDesc=$errorDesc"
                        }
                    }
                }
            )
    }
}