@file:JvmName("ViewCompatUtils")

package biz.growapp.base.extensions

import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

inline fun View.show() {
    visibility = View.VISIBLE
}

inline fun View.hide() {
    visibility = View.GONE
}

inline fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.isVisible() = visibility == View.VISIBLE

var View.elevationCompat: Float
    get() = ViewCompat.getElevation(this)
    set(value) = ViewCompat.setElevation(this, value)

inline fun View.onGlobalLayout(crossinline action: () -> Unit): ViewTreeObserver.OnGlobalLayoutListener {
    val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action()
        }
    }
    viewTreeObserver.addOnGlobalLayoutListener(listener)
    return listener
}

inline fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
}
