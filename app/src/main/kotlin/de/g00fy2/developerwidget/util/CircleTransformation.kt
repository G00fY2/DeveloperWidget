package de.g00fy2.developerwidget.util

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.squareup.picasso3.RequestHandler
import com.squareup.picasso3.Transformation

class CircleTransformation : Transformation {

  override fun transform(source: RequestHandler.Result): RequestHandler.Result {
    return if (source.bitmap != null) {
      var bitmap: Bitmap = source.bitmap!!
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

      val circleBitmap = Bitmap.createBitmap(size, size, bitmap.config)
      val canvas = Canvas(circleBitmap)
      val paint = Paint()
      val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

      paint.shader = shader
      paint.isAntiAlias = true
      val centerAndRadius = size / 2f
      canvas.drawCircle(centerAndRadius, centerAndRadius, centerAndRadius, paint)

      bitmap.recycle()
      RequestHandler.Result(circleBitmap, source.loadedFrom, source.exifRotation)
    } else {
      source
    }
  }

  override fun key(): String {
    return "circleTransformation()"
  }
}