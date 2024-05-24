package org.hyperskill.app.android.topic_completion.model

data class TopicCompletedModalViewState(
    val title: String = "{Topic name} completed!",
    val description: String = "Great job, you are one step closer to your goal—keep up the good work!",
    val earnedGemsText: String = "+ 5",
    val callToActionButtonTitle: String = "Continue with next topic",
    val spacebotAvatarVariantIndex: Int = 0,
    val backgroundAnimationStyle: BackgroundAnimationStyle = BackgroundAnimationStyle.FIRST
) {
    enum class BackgroundAnimationStyle {
        FIRST, SECOND
    }
}
