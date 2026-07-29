package com.example.barcodescanner.usecase

import android.graphics.Bitmap
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers

object BarcodeImageScanner {
    private var bitmapBuffer: IntArray? = null

    fun parse(image: Bitmap): Single<Result> {
        return Single
            .create<Result> { emitter ->
                parse(image, emitter)
            }
            .subscribeOn(Schedulers.newThread())
    }

    private fun parse(image: Bitmap, emitter: SingleEmitter<Result>) {
        try {
            emitter.onSuccess(tryParse(image))
        } catch (ex: Exception) {
            Logger.log(ex)
            emitter.onError(ex)
        }
    }

    private fun tryParse(image: Bitmap): Result {
        val width = image.width
        val height = image.height
        val size = width * height

        var buffer = bitmapBuffer
        if (buffer == null || size > buffer.size) {
            buffer = IntArray(size)
            bitmapBuffer = buffer
        }

        image.getPixels(buffer, 0, width, 0, 0, width, height)

        val source = RGBLuminanceSource(width, height, buffer)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        val reader = MultiFormatReader()
        return reader.decode(bitmap)
    }
}