package org.hyperskill.app.auth.domain.exception

import org.hyperskill.app.auth.domain.model.AuthCredentialsError

class AuthCredentialsException(val authCredentialsError: AuthCredentialsError) : Exception()