@file:Suppress("unused")

package com.asapp.churros

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast

fun Context.toast(text: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, duration).show()
}

fun Context.toast(@StringRes textResId: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, textResId, duration).show()
}