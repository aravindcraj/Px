package com.aravindcraj.px.utils

import android.util.Log
import com.aravindcraj.px.BuildConfig.DEBUG

internal object Logger {
    private const val TAG = "PxLogger"

    inline fun i(crossinline block: () -> String) {
        if (DEBUG) {
            Log.i(TAG, block())
        }
    }
}