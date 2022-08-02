package org.hyperskill.app.notification.data.source

interface NotificationCacheDataSource {
    suspend fun getNotificationsEnabled(): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean)

    // TODO: для этих методов хотел заюзать kotlinx.datetime.LocalTime
    //  тут видимо будет храниться метка времени, которая в профиле настраивается
    //  в теории можно обойтись Int, сохраняя просто номер часа
    suspend fun getNotificationsTimestamp(): Int
    suspend fun setNotificationsTimestamp(timestamp: Int)
}