package org.hyperskill.app.stage_implement.view.mapper

import org.hyperskill.app.stage_implement.presentation.StageImplementFeature

internal object StageImplementViewStateMapper {
    fun mapState(state: StageImplementFeature.State): StageImplementFeature.ViewState =
        when (state) {
            is StageImplementFeature.State.Idle ->
                StageImplementFeature.ViewState.Idle
            is StageImplementFeature.State.Loading ->
                StageImplementFeature.ViewState.Loading
            is StageImplementFeature.State.Deprecated ->
                StageImplementFeature.ViewState.Deprecated
            is StageImplementFeature.State.Unsupported ->
                StageImplementFeature.ViewState.Unsupported
            is StageImplementFeature.State.NetworkError ->
                StageImplementFeature.ViewState.NetworkError
            is StageImplementFeature.State.Content ->
                StageImplementFeature.ViewState.Content(state.project, state.stage, state.step)
        }
}