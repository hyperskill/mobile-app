package org.hyperskill.app.profile.injection

import kotlinx.coroutines.flow.MutableSharedFlow

class ProfileHypercoinsDataComponentImpl : ProfileHypercoinsDataComponent {
    override val hypercoinsBalanceMutableSharedFlow: MutableSharedFlow<Int> = MutableSharedFlow()
}