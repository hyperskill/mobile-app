package org.hyperskill.app.android.core.extensions

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.HyperskillApp

object ShareUtils {
    fun getShareDrawableIntent(
        context: Context,
        @DrawableRes drawableRes: Int,
        text: String,
        title: String
    ): Intent {
        val shareIntent = Intent().apply {
            val bitmap = BitmapFactory.decodeResource(context.resources, drawableRes)

            val originImageUri = getUriForBitmap(context, bitmap, "$drawableRes.png")

            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, originImageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_TEXT, text)
            clipData = ClipData.newRawUri(null, originImageUri)
            type = "image/*"
        }
        return Intent.createChooser(
            shareIntent,
            title
        )
    }

    private fun getUriForBitmap(
        context: Context,
        bitmap: Bitmap,
        name: String
    ): Uri {
        val file = File(context.cacheDir, name).apply {
            createNewFile()
        }
        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        return FileProvider.getUriForFile(
            HyperskillApp.getAppContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            file
        )
    }

    fun getShareTextIntent(text: String): Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
}