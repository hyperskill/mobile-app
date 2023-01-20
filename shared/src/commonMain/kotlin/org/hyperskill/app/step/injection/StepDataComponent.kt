package org.hyperskill.app.step.injection

import org.hyperskill.app.step.domain.interactor.StepInteractor

interface StepDataComponent {
    val stepInteractor: StepInteractor
}