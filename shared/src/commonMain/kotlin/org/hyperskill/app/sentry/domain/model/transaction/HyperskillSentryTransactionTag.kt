package org.hyperskill.app.sentry.domain.model.transaction

/**
 * Represents a Sentry transaction tag.
 *
 * @property key The tag key.
 * @property value The tag value.
 */
sealed class HyperskillSentryTransactionTag {
    abstract val key: String
    abstract val value: String

    sealed class User : HyperskillSentryTransactionTag() {
        override val key: String = "user"

        class IsAuthorized(isAuthorized: Boolean) : User() {
            override val key: String =
                "${super.key}.is_authorized"

            override val value: String =
                isAuthorized.toString()
        }
    }

    sealed class StudyPlanSection : HyperskillSentryTransactionTag() {
        override val key: String = "study_plan_section"

        class IsCurrent(isCurrent: Boolean) : StudyPlanSection() {
            override val key: String =
                "${super.key}.is_current"

            override val value: String =
                isCurrent.toString()
        }
    }
}