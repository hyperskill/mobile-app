package org.hyperskill.app.topic_completed_modal.view

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.State
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState

internal class TopicCompletedModalViewStateMapper(
    private val resourceProvider: ResourceProvider
) {
    companion object {
        internal const val SPACEBOT_AVATAR_VARIANT_COUNT = 20

        private val DESCRIPTION_RESOURCES = listOf(
            SharedResources.strings.topic_completed_modal_description_1,
            SharedResources.strings.topic_completed_modal_description_2,
            SharedResources.strings.topic_completed_modal_description_3,
            SharedResources.strings.topic_completed_modal_description_4,
            SharedResources.strings.topic_completed_modal_description_5,
            SharedResources.strings.topic_completed_modal_description_6,
            SharedResources.strings.topic_completed_modal_description_7,
            SharedResources.strings.topic_completed_modal_description_8,
            SharedResources.strings.topic_completed_modal_description_9,
            SharedResources.strings.topic_completed_modal_description_10,
            SharedResources.strings.topic_completed_modal_description_11,
            SharedResources.strings.topic_completed_modal_description_12,
            SharedResources.strings.topic_completed_modal_description_13,
            SharedResources.strings.topic_completed_modal_description_14,
            SharedResources.strings.topic_completed_modal_description_15,
            SharedResources.strings.topic_completed_modal_description_16,
            SharedResources.strings.topic_completed_modal_description_17,
            SharedResources.strings.topic_completed_modal_description_18,
            SharedResources.strings.topic_completed_modal_description_19,
            SharedResources.strings.topic_completed_modal_description_20
        )
    }

    fun map(state: State): ViewState =
        ViewState(
            title = resourceProvider.getString(
                SharedResources.strings.topic_completed_modal_title_template,
                state.topic.title
            ),
            description = getDescription(state.passedTopicsCount),
            callToActionButtonTitle = resourceProvider.getString(
                if (state.canContinueWithNextTopic) {
                    SharedResources.strings.topic_completed_modal_continue_with_next_topic_button_text
                } else {
                    SharedResources.strings.go_to_study_plan
                }
            ),
            spacebotAvatarVariantIndex = state.passedTopicsCount % SPACEBOT_AVATAR_VARIANT_COUNT,
            backgroundAnimationStyle = getBackgroundAnimationStyle(state.passedTopicsCount)
        )

    private fun getDescription(passedTopicsCount: Int): String {
        val stringResource = DESCRIPTION_RESOURCES[passedTopicsCount % DESCRIPTION_RESOURCES.size]
        return resourceProvider.getString(stringResource)
    }

    private fun getBackgroundAnimationStyle(passedTopicsCount: Int): ViewState.BackgroundAnimationStyle {
        val styles = ViewState.BackgroundAnimationStyle.values()
        return styles[passedTopicsCount % styles.size]
    }
}