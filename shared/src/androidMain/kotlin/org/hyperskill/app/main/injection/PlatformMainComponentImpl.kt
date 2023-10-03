package org.hyperskill.app.main.injection

import org.hyperskill.app.analytic.injection.AnalyticComponent
import org.hyperskill.app.core.injection.SavedStateReduxViewModelFactory
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.main.presentation.MainViewModel

class PlatformMainComponentImpl(
    private val mainComponent: MainComponent,
    private val analyticComponent: AnalyticComponent
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
                        feature,
                        savedStateHandle,
                        analyticComponent.analyticInteractor
                    )
                }
            )
        )
}