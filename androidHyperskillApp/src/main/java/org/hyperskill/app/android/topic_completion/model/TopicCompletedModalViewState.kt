package org.hyperskill.app.android.topic_completion.model

data class TopicCompletedModalViewState(
    val title: String = "{Topic name} completed!",
    val description: String = "Learning might be tough, but it brings you knowledge that lasts forever",
    val earnedGemsText: String = "+ 5",
    val callToActionButtonTitle: String = "Continue with next topic",
    val spacebotAvatarVariantIndex: Int = 0,
    val backgroundAnimationStyle: BackgroundAnimationStyle = BackgroundAnimationStyle.FIRST
) {
    enum class BackgroundAnimationStyle {
        FIRST, SECOND
    }
}
