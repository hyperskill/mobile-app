package org.hyperskill.topic_completed_modal

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature
import org.hyperskill.app.topic_completed_modal.view.TopicCompletedModalViewStateMapper
import org.hyperskill.app.topics.domain.model.Topic

class TopicCompletedModalViewStateMapperTest {
    private val viewStateMapper = TopicCompletedModalViewStateMapper(ResourceProviderStub())

    @Test
    fun `spacebotAvatarVariantIndex is correctly calculated`() {
        val passedTopicsCount = 21
        val state = createState(passedTopicsCount)
        val viewState = viewStateMapper.map(state)

        val expectedAvatarVariantIndex =
            passedTopicsCount % TopicCompletedModalViewStateMapper.SPACEBOT_AVATAR_VARIANT_COUNT

        assertEquals(expectedAvatarVariantIndex, viewState.spacebotAvatarVariantIndex)
    }

    @Test
    fun `backgroundAnimationStyle is correctly calculated`() {
        val passedTopicsCount = 3
        val state = createState(passedTopicsCount)
        val viewState = viewStateMapper.map(state)

        val styles = TopicCompletedModalFeature.ViewState.BackgroundAnimationStyle.values()
        val expectedBackgroundAnimationStyle = styles[passedTopicsCount % styles.size]

        assertEquals(expectedBackgroundAnimationStyle, viewState.backgroundAnimationStyle)
    }

    @Test
    fun `test spacebotAvatarVariantIndex and backgroundAnimationStyle for boundary value`() {
        val passedTopicsCount = 0
        val state = createState(passedTopicsCount)
        val viewState = viewStateMapper.map(state)

        val expectedAvatarVariantIndex =
            passedTopicsCount % TopicCompletedModalViewStateMapper.SPACEBOT_AVATAR_VARIANT_COUNT
        val styles = TopicCompletedModalFeature.ViewState.BackgroundAnimationStyle.values()
        val expectedBackgroundAnimationStyle = styles[passedTopicsCount % styles.size]

        assertEquals(expectedAvatarVariantIndex, viewState.spacebotAvatarVariantIndex)
        assertEquals(expectedBackgroundAnimationStyle, viewState.backgroundAnimationStyle)
    }

    @Test
    fun `test spacebotAvatarVariantIndex and backgroundAnimationStyle for maximum value`() {
        val passedTopicsCount = Int.MAX_VALUE
        val state = createState(passedTopicsCount)
        val viewState = viewStateMapper.map(state)

        val expectedAvatarVariantIndex =
            passedTopicsCount % TopicCompletedModalViewStateMapper.SPACEBOT_AVATAR_VARIANT_COUNT
        val styles = TopicCompletedModalFeature.ViewState.BackgroundAnimationStyle.values()
        val expectedBackgroundAnimationStyle = styles[passedTopicsCount % styles.size]

        assertEquals(expectedAvatarVariantIndex, viewState.spacebotAvatarVariantIndex)
        assertEquals(expectedBackgroundAnimationStyle, viewState.backgroundAnimationStyle)
    }

    private fun createState(passedTopicsCount: Int): TopicCompletedModalFeature.State =
        TopicCompletedModalFeature.State(
            topic = Topic(id = 1, title = "Sample Topic", progressId = ""),
            passedTopicsCount = passedTopicsCount,
            canContinueWithNextTopic = true
        )
}