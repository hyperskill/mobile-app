package org.hyperskill.app.profile.injection

import kotlinx.coroutines.flow.MutableSharedFlow

interface ProfileHypercoinsDataComponent {
    val hypercoinsBalanceMutableSharedFlow: MutableSharedFlow<Int>
}