package org.hyperskill.app.main.presentation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.chrynan.parcelable.core.Parcelable
import com.chrynan.parcelable.core.decodeFromBundle
import com.chrynan.parcelable.core.encodeToBundle
import kotlinx.serialization.ExperimentalSerializationApi
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.main.injection.PlatformMainComponentImpl
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.core.model.Cancellable
import ru.nobird.app.presentation.redux.container.ReduxViewContainer
import ru.nobird.app.presentation.redux.feature.Feature

@OptIn(ExperimentalSerializationApi::class)
/**
 * Sent [AppFeature.Message.Initialize] only if the state was not saved before in [SavedStateHandle].
 * If the state was saved, then [AppFeature] is initialized with the saved state.
 * @see [PlatformMainComponentImpl], [MainViewModel.decodeState]
 */
class MainViewModel(
    reduxViewContainer: ReduxViewContainer<AppFeature.State, AppFeature.Message, AppFeature.Action.ViewAction>,
    feature: Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>,
    private val savedStateHandle: SavedStateHandle,
    private val analyticInteractor: AnalyticInteractor
) : ReduxViewModel<AppFeature.State, AppFeature.Message, AppFeature.Action.ViewAction>(reduxViewContainer) {
    companion object {
        private const val STATE_KEY = "AppFeatureState"

        fun encodeState(state: AppFeature.State): Bundle =
            Parcelable.Default.encodeToBundle(state, AppFeature.State.serializer())

        /**
         * Decodes state from [SavedStateHandle]
         * @return [AppFeature.State] or null if state is not saved
         */
        fun decodeState(savedStateHandle: SavedStateHandle): AppFeature.State? {
            val savedStateBundle: Bundle? = savedStateHandle[STATE_KEY]
            return savedStateBundle?.let {
                Parcelable.Default.decodeFromBundle(
                    bundle = it,
                    deserializer = AppFeature.State.serializer()
                )
            }
        }
    }

    private val wasStateSaved: Boolean
        get() = savedStateHandle.contains(STATE_KEY)

    private val stateListener: Cancellable

    init {
        stateListener = feature.addStateListener { state ->
            savedStateHandle[STATE_KEY] = encodeState(state)
        }
    }

    fun startup(pushNotificationData: PushNotificationData? = null) {
        if (!wasStateSaved) {
            onNewMessage(AppFeature.Message.Initialize(pushNotificationData))
        } else if (pushNotificationData != null) {
            onNewMessage(AppFeature.Message.NotificationClicked(pushNotificationData))
        }
    }

    override fun onCleared() {
        super.onCleared()
        stateListener.cancel()
    }

    fun logScreenOrientation(screenOrientation: ScreenOrientation) {
        analyticInteractor.setScreenOrientation(screenOrientation)
    }
}