package com.shadhinmusiclibrary.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.TypedValue
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

internal object ImageSizeParser {
    fun getDPfromPX(pixel: Int, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            pixel.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun getSPfromPX(pixel: Int, context: Context): Int {
        return (pixel * context.resources.displayMetrics.scaledDensity).toInt()
    }

    fun getLocalBitmapUri(bmp: Bitmap, context: Context): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png"
            )
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            //bmpUri = Uri.fromFile(file);
            //bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            bmpUri = FileProvider.getUriForFile(
                context, context.applicationContext
                    .packageName + ".provider", file
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}