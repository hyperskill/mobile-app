package org.hyperskill.app.profile.data.source

import org.hyperskill.app.core.domain.repository.StateHolder
import org.hyperskill.app.profile.domain.model.Profile

interface CurrentProfileStateHolder : StateHolder<Profile>