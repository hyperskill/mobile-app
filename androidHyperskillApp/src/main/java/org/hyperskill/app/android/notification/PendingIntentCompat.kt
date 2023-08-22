package org.hyperskill.app.android.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

object PendingIntentCompat {
    fun getBroadcast(
        context: Context,
        requestCode: Int,
        intent: Intent,
        flags: Int,
        isMutable: Boolean = false
    ): PendingIntent =
        PendingIntent.getBroadcast(context, requestCode, intent, getFlags(flags, isMutable))

    fun getActivity(
        context: Context,
        requestCode: Int,
        intent: Intent,
        flags: Int,
        isMutable: Boolean = false
    ): PendingIntent =
        PendingIntent.getActivity(context, requestCode, intent, getFlags(flags, isMutable))

    @SuppressLint("ObsoleteSdkInt")
    fun getFlags(flags: Int, isMutable: Boolean): Int =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val mutableFlag =
                    if (isMutable) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_IMMUTABLE
                flags or mutableFlag
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isMutable ->
                flags or PendingIntent.FLAG_IMMUTABLE

            else -> flags
        }
}