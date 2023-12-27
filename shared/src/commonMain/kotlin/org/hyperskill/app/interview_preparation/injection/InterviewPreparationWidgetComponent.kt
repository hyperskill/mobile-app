package org.hyperskill.app.interview_preparation.injection

import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetActionDispatcher
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetReducer

interface InterviewPreparationWidgetComponent {
    val interviewPreparationWidgetReducer: InterviewPreparationWidgetReducer
    val interviewPreparationWidgetActionDispatcher: InterviewPreparationWidgetActionDispatcher
}