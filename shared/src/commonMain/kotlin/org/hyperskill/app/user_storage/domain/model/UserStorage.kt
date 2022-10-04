package org.hyperskill.app.user_storage.domain.model

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

typealias UserStorage = JsonObject

typealias UserStorageKey = String

typealias UserStorageValue = JsonElement