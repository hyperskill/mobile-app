package org.hyperskill.app.step_quiz.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.step_quiz.data.source.SubmissionCacheDataSource

class SubmissionCacheDataSourceImpl(
    private val settings: Settings
) : SubmissionCacheDataSource {
    override fun incrementStepsInAppSolved() {
        settings.putLong(SubmissionCacheKeyValues.STEPS_SOLVED_IN_APP, getStepsInAppSolved() + 1)
    }

    override fun getStepsInAppSolved(): Long =
        settings.getLong(SubmissionCacheKeyValues.STEPS_SOLVED_IN_APP, 0)
}