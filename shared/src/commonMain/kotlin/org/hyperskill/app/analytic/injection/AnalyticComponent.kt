package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor

interface AnalyticComponent {
    val analyticInteractor: AnalyticInteractor
}