package com.example.advanced.shoppingmall.extensions

import android.content.res.Resources

// Convert Dp To Pixel
internal fun Float.fromDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}