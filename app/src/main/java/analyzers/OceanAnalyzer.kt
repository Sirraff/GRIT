package analyzers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import android.graphics.Rect
import java.nio.ByteBuffer
class OceanAnalyzer(private val onColorCalculated: (Int) -> Unit) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val bitmap = imageProxy.toBitmap() // Convert to bitmap (see below for this method)

        // TODO: Implement the logic to extract pixel data from the bitmap
        // and calculate the median color

        // Dummy color calculation for now
        val medianColor = bitmap?.let { calculateMedianColor(it) }

        if (medianColor != null) {
            onColorCalculated(medianColor)
        }
        imageProxy.close()
    }

    private fun calculateMedianColor(bitmap: Bitmap): Int {
        // TODO
        // Define the coordinates of the green square if necessary
        // For simplicity, this  ses the entire bitmap
        val width = bitmap.width
        val height = bitmap.height

        var redSum = 0L
        var greenSum = 0L
        var blueSum = 0L
        var pixelCount = 0L

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = bitmap.getPixel(x, y)
                redSum += (pixel shr 16 and 0xFF)
                greenSum += (pixel shr 8 and 0xFF)
                blueSum += (pixel and 0xFF)
                pixelCount++
            }
        }

        // Calculate average colors
        val red = (redSum / pixelCount).toInt()
        val green = (greenSum / pixelCount).toInt()
        val blue = (blueSum / pixelCount).toInt()

        // Combine average red, green, blue values to get the final color
        return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    }

    fun ImageProxy.toBitmap(): Bitmap? {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        // U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, this.width, this.height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}