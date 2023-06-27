package org.hyperskill.app.android.core.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import ru.nobird.android.view.base.ui.extension.resolveColorAttribute

object NotificationChannelInitializer {
    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // channels were introduced only in O. Before we had used in-app channels
            return
        }

        val hyperskillNotificationChannel = HyperskillNotificationChannel.values()
        val androidChannels = ArrayList<NotificationChannel>(hyperskillNotificationChannel.size)
        hyperskillNotificationChannel.forEach {
            androidChannels.add(initChannel(context, it))
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(androidChannels)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChannel(
        context: Context,
        hyperskillChannel: HyperskillNotificationChannel
    ): NotificationChannel {
        val channelName = context.getString(hyperskillChannel.visibleChannelNameRes)
        val channel = NotificationChannel(hyperskillChannel.channelId, channelName, hyperskillChannel.importance)
        hyperskillChannel.visibleChannelDescriptionRes?.let {
            channel.description = context.getString(it)
        }
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = context.resolveColorAttribute(androidx.appcompat.R.attr.colorError)
        return channel
    }
}