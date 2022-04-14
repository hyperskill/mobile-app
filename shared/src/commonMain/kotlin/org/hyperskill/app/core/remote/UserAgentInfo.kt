package org.hyperskill.app.core.remote

data class UserAgentInfo(
    val versionName: String,
    val osVersion: String,
    val versionCode: String,
    val applicationId: String
) {
    override fun toString(): String =
        "Hyperskill-Mobile/$versionName ($osVersion) build/$versionCode package/$applicationId"
}