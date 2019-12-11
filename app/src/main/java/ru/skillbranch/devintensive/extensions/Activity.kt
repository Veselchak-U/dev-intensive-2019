package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val view: View? = this.getCurrentFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.isKeyboardOpen(): Boolean {
    var rect = Rect();
    this.window.decorView.getWindowVisibleDisplayFrame(rect)

    val point = Point()
    windowManager.defaultDisplay.getSize(point)

    if((point.y - rect.bottom) > 100)
        return true

    return false
}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardOpen()
}