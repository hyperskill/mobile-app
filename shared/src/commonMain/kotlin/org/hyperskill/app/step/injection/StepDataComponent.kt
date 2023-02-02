package org.hyperskill.app.step.injection

import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.repository.StepRepository

interface StepDataComponent {
    val stepRepository: StepRepository
    val stepInteractor: StepInteractor
}