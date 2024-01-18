package org.hyperskill.app.profile.presentation

import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProfileViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, Action.ViewAction>,
    private val purchaseInteractor: PurchaseInteractor
) : ReduxViewModel<State, Message, Action.ViewAction>(reduxViewContainer) {
    suspend fun getManagementUrl(): Result<String?> =
        purchaseInteractor.getManagementUrl()

    suspend fun purchaseMobileOnlySubscription(): Result<PurchaseResult> =
        purchaseInteractor.purchaseMobileOnlySubscription()
}