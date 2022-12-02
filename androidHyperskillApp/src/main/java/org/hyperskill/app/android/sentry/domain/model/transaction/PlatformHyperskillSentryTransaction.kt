package org.hyperskill.app.android.sentry.domain.model.transaction

import io.sentry.ITransaction
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransaction

class PlatformHyperskillSentryTransaction(
    val transaction: ITransaction
) : HyperskillSentryTransaction(transaction.name, transaction.operation)