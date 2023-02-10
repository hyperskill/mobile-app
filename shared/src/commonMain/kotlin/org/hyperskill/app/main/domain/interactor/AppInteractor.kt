package org.hyperskill.app.main.domain.interactor

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.user_storage.domain.interactor.UserStorageInteractor

class AppInteractor(
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    private val userStorageInteractor: UserStorageInteractor,
    private val analyticInteractor: AnalyticInteractor,
) {
    suspend fun doCurrentUserSignedOutCleanUp() {
        analyticInteractor.flushEvents()
        clearCache()
    }

    private suspend fun clearCache() {
        authInteractor.clearCache()
        profileInteractor.clearCache()
        userStorageInteractor.clearCache()
    }
}