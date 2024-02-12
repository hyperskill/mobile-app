package org.hyperskill.app.manage_subscription.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionViewModel

class PlatformManageSubscriptionComponentImpl(
    private val manageSubscriptionComponent: ManageSubscriptionComponent
) : PlatformManageSubscriptionComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                ManageSubscriptionViewModel::class.java to {
                    ManageSubscriptionViewModel(
                        manageSubscriptionComponent.manageSubscriptionFeature.wrapWithFlowView()
                    )
                }
            )
        )
}