package org.hyperskill.app.auth.domain.exception

import org.hyperskill.app.auth.domain.model.AuthSocialError

class AuthSocialException(val authSocialError: AuthSocialError) : Exception()