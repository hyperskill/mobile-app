package org.hyperskill.app.submissions.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.submissions.data.source.SubmissionsCacheDataSource

internal class SubmissionsCacheDataSourceImpl(
    private val settings: Settings
) : SubmissionsCacheDataSource {
    @Suppress("MagicNumber")
    override fun incrementSolvedStepsCount() {
        settings.putLong(SubmissionsCacheKeyValues.STEPS_SOLVED_COUNT, getSolvedStepsCount() + 1)
    }

    override fun getSolvedStepsCount(): Long =
        settings.getLong(SubmissionsCacheKeyValues.STEPS_SOLVED_COUNT)
}