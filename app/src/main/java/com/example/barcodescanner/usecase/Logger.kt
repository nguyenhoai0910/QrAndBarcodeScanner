package com.example.barcodescanner.usecase

import android.util.Log
import com.example.barcodescanner.BuildConfig

object Logger {
    var isEnabled = BuildConfig.ERROR_REPORTS_ENABLED_BY_DEFAULT

    fun log(error: Throwable) {
        if (isEnabled) {
            Log.e("BarcodeScanner", "Unhandled error", error)
        }
    }
}
