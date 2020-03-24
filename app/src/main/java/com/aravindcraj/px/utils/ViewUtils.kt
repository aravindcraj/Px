package com.aravindcraj.px.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun Fragment.hideKeyboard() {
    val windowToken = this.view?.rootView?.windowToken
    val imm = context?.getSystemService(
        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
    view?.clearFocus()
}