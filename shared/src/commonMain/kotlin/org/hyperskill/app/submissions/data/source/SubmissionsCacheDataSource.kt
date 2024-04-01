package org.hyperskill.app.submissions.data.source

interface SubmissionsCacheDataSource {
    fun incrementSolvedStepsCount()
    fun getSolvedStepsCount(): Long
}