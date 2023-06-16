package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.SavedStateReduxViewModelFactory
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel
import org.hyperskill.app.push_notifications.injection.PlatformPushNotificationsComponent
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformMainComponentImpl(
    private val mainComponent: MainComponent,
    private val platformPushNotificationsComponent: PlatformPushNotificationsComponent,
) : PlatformMainComponent {
    override val reduxViewModelFactory: SavedStateReduxViewModelFactory
        get() = SavedStateReduxViewModelFactory(
            mapOf(
                MainViewModel::class.java to { savedStateHandle ->
                    /**
                     * If the state was saved, then [AppFeature] is initialized with the saved state.
                     */
                    val initialState = MainViewModel.decodeState(savedStateHandle)
                    val feature = mainComponent.appFeature(initialState)
                    MainViewModel(
                        feature.wrapWithViewContainer(),
                        feature,
                        savedStateHandle,
                        platformPushNotificationsComponent.pushNotificationDeviceRegistrar
                    )
                }
            )
        )
}