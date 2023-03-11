package org.hyperskill.app.stages.injection

import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.stages.domain.repository.StagesRepository

interface StagesDataComponent {
    val stagesRepository: StagesRepository
    val stagesInteractor: StagesInteractor
}