package org.hyperskill.app.network.domain.model

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo

class AuthorizedClientDependencies(
    val networkEndpointConfigInfo: NetworkEndpointConfigInfo,
    val userAgentInfo: UserAgentInfo,
    val json: Json,
    val settings: Settings,
    val buildVariant: BuildVariant,
    val authorizationFlow: MutableSharedFlow<UserDeauthorized>,
    val authorizationMutex: Mutex,
    val cookiesStorage: CookiesStorage,
    val logger: Logger
)