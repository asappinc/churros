package com.asapp.churros

import android.view.View

var View.isPresent: Boolean
    get() = visibility != View.GONE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }