package org.hyperskill.app.user_list.remote.model

data class UsersQuery(
    val page: Int = 1,
    val pageSize: Int = 10,
    val userName: String? = null
)
