package org.hyperskill.app.freemium.injection

import org.hyperskill.app.freemium.domain.interactor.FreemiumInteractor

interface FreemiumDataComponent {
    val freemiumInteractor: FreemiumInteractor
}