package org.hyperskill.app.profile.presentation

import android.app.Activity
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.profile.presentation.ProfileFeature.State
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.purchases.domain.model.AndroidPurchaseParams
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class ProfileViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, Action.ViewAction>,
    private val purchaseInteractor: PurchaseInteractor
) : ReduxViewModel<State, Message, Action.ViewAction>(reduxViewContainer) {

    private var _purchaseButtonState: MutableStateFlow<PurchaseButtonState> =
        MutableStateFlow(PurchaseButtonState.Initial)
    val purchaseButtonState: StateFlow<PurchaseButtonState>
        get() = _purchaseButtonState

    val toastMessages: Channel<String> = Channel()

    init {
        viewModelScope.launch {
            val managementUrl = purchaseInteractor.getManagementUrl().getOrNull()
            _purchaseButtonState.value = if (managementUrl != null) {
                PurchaseButtonState.ManageSubscription(managementUrl)
            } else {
                PurchaseButtonState.Buy
            }
        }
    }

    fun onBuySubscriptionClick(
        activity: Activity
    ) {
        viewModelScope.launch {
            val purchaseResult =
                purchaseInteractor.purchaseMobileOnlySubscription(
                    AndroidPurchaseParams(activity)
                ).getOrElse {
                    toastMessages.send("Unknown error happened!")
                    return@launch
                }
            if (purchaseResult is PurchaseResult.Succeed) {
                _purchaseButtonState.value =
                    PurchaseButtonState.ManageSubscription(
                        purchaseInteractor.getManagementUrl().getOrNull()
                    )
            }
            toastMessages.send(
                when (purchaseResult) {
                    is PurchaseResult.Succeed -> "Purchase succeed"
                    PurchaseResult.CancelledByUser -> "Purchase cancelled by user"
                    is PurchaseResult.Error -> purchaseResult.message
                }
            )
        }
    }

    sealed interface PurchaseButtonState {
        object Initial : PurchaseButtonState
        object Buy : PurchaseButtonState

        data class ManageSubscription(val managementUrl: String?): PurchaseButtonState
    }
}