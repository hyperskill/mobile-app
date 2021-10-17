package org.hyperskill.app.user_list.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.hyperskill.app.user_list.domain.model.User

@Serializable
data class UsersResponse(
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    @SerialName("items")
    val items: List<User>,
    @SerialName("total_count")
    val totalCount: Int
)