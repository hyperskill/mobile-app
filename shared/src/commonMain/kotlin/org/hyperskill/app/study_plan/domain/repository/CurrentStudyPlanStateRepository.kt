package org.hyperskill.app.study_plan.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.study_plan.domain.model.StudyPlan

interface CurrentStudyPlanStateRepository : StateRepository<StudyPlan>