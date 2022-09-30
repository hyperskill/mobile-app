package org.hyperskill.app.user_storage.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.user_storage.domain.model.UserStorage

@Serializable
class UserStorageResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("user-storages")
    val userStorages: List<UserStorage>
) : MetaResponse