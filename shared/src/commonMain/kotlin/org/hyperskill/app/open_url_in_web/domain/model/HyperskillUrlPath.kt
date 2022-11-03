package org.hyperskill.app.open_url_in_web.domain.model

sealed class HyperskillUrlPath {
    abstract val path: String

    class Index : HyperskillUrlPath() {
        override val path: String = "/"
    }

    class Register : HyperskillUrlPath() {
        override val path: String = "/register"
    }

    class Profile(profileId: Long) : HyperskillUrlPath() {
        override val path: String = "/profile/$profileId"
    }

    class Track(trackId: Long) : HyperskillUrlPath() {
        override val path: String = "/tracks/$trackId"
    }

    class StudyPlan : HyperskillUrlPath() {
        override val path: String = "/study-plan"
    }

    class ResetPassword : HyperskillUrlPath() {
        override val path: String = "/accounts/password/reset"
    }

    class DeleteAccount : HyperskillUrlPath() {
        override val path: String = "/delete-account"
    }
}
