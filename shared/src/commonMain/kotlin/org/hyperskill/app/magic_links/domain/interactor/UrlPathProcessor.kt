package org.hyperskill.app.magic_links.domain.interactor

import io.ktor.http.fullPath
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.magic_links.domain.model.MagicLink

class UrlPathProcessor(private val magicLinksInteractor: MagicLinksInteractor) {

    /**
     * Create **MagicLink** if necessary or build plain url.
     *
     * @param path Url path to process
     * @return On success returns a value of String represented **Magic link** or plain url. Otherwise returns a failure with an arbitrary Throwable exception.
     *
     * @see MagicLink
     * @see MagicLinksInteractor
     * */
    suspend fun processUrlPath(
        path: HyperskillUrlPath
    ): Result<String> =
        if (shouldCreateMagicLink(path)) {
            val redirectUrl = HyperskillUrlBuilder.build(path).fullPath
            magicLinksInteractor.createMagicLink(redirectUrl).map { it.url }
        } else {
            Result.success(HyperskillUrlBuilder.build(path).toString())
        }

    /**
     * Checks that **MagicLink** should be created.
     *
     * @param path Next redirect url.
     * @return Returns true if **MagicLink** should be created.
     */
    private fun shouldCreateMagicLink(path: HyperskillUrlPath): Boolean =
        when (path) {
            is HyperskillUrlPath.DeleteAccount -> true
            is HyperskillUrlPath.Index -> true
            is HyperskillUrlPath.Profile -> true
            is HyperskillUrlPath.Register -> false
            is HyperskillUrlPath.ResetPassword -> false
            is HyperskillUrlPath.StudyPlan -> true
            is HyperskillUrlPath.Track -> true
        }
}