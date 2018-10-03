package de.g00fy2.developerwidget.util

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.squareup.picasso3.RequestHandler
import com.squareup.picasso3.Transformation

class CircleTransformation : Transformation {

  override fun transform(source: RequestHandler.Result): RequestHandler.Result {
    if (source.bitmap == null) {
      return source
    }

    var bitmap: Bitmap

    // since we cant transform hardware bitmaps create a software copy first
    if (VERSION.SDK_INT >= VERSION_CODES.O && source.bitmap!!.config == Config.HARDWARE) {
      val softwareCopy = source.bitmap!!.copy(Config.ARGB_8888, true)
      if (softwareCopy == null) {
        return source
      } else {
        bitmap = softwareCopy
        source.bitmap!!.recycle()
      }
    } else {
      bitmap = source.bitmap!!
    }

    var size = bitmap.width
    // if bitmap is non-square first create square one
    if (size != bitmap.height) {
      var sizeX = size
      var sizeY = bitmap.height
      size = Math.min(sizeY, sizeX)
      sizeX = (sizeX - size) / 2
      sizeY = (sizeY - size) / 2

      val squareSource = Bitmap.createBitmap(bitmap, sizeX, sizeY, size, size)
      bitmap.recycle()
      bitmap = squareSource
    }

    val circleBitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888)
    val canvas = Canvas(circleBitmap)
    val paint = Paint()
    val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    paint.shader = shader
    paint.isAntiAlias = true
    val centerAndRadius = size / 2f
    canvas.drawCircle(centerAndRadius, centerAndRadius, centerAndRadius, paint)

    bitmap.recycle()
    return RequestHandler.Result(circleBitmap, source.loadedFrom, source.exifRotation)
  }

  override fun key(): String {
    return "circleTransformation()"
  }
}