package org.hyperskill.app.core.domain.url

import org.hyperskill.app.step.domain.model.StepRoute

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

    class Step(stepRoute: StepRoute) : HyperskillUrlPath() {
        override val path: String = stepRoute.analyticRoute.path
    }
}