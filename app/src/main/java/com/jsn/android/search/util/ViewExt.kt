package com.jsn.android.search

import android.annotation.SuppressLint
import android.os.Build
import android.view.DisplayCutout
import android.view.View
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter




/** Combination of all flags required to put activity into immersive mode */
@SuppressLint("InlinedApi")
const val FLAGS_FULLSCREEN =
        View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION


/** Pad this view with the insets provided by the device cutout (i.e. notch) */
@RequiresApi(Build.VERSION_CODES.P)
fun View.padWithDisplayCutout() {

    /** Helper method that applies padding from cutout's safe insets */
    fun doPadding(cutout: DisplayCutout) = setPadding(
            cutout.safeInsetLeft,
            cutout.safeInsetTop,
            cutout.safeInsetRight,
            cutout.safeInsetBottom)

    // Apply padding using the display cutout designated "safe area"
    rootWindowInsets?.displayCutout?.let { doPadding(it) }

    // Set a listener for window insets since view.rootWindowInsets may not be ready yet
    setOnApplyWindowInsetsListener { view, insets ->
        insets.displayCutout?.let { doPadding(it) }
        insets
    }
}


@BindingAdapter("paddingLeftSystemWindowInsets",
        "paddingRightSystemWindowInsets"
        ,"paddingTopSystemWindowInsets"
        ,"paddinBottomSystemWindowInsets",
        requireAll = false)
fun applySystwmWindows(
        view: View,
        applyLeft:Boolean=false,
        applyRight:Boolean=false,
        applyTop:Boolean=false,
        applyBottom:Boolean=false
){
    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT_WATCH) return

    view.doOnApplyWindowInsets{ view, insets, padding ->
        val left= if(applyLeft) insets.systemWindowInsetLeft else 0
        val right= if(applyRight) insets.systemWindowInsetRight else 0
        val top= if(applyTop) insets.systemWindowInsetTop else 0
        val bottom= if(applyBottom) insets.systemWindowInsetBottom else 0
        view.setPadding(
                padding.left+left,
                padding.right+right,
                padding.top+top,
                padding.bottom+bottom
        )
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }
            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}
data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
        view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

inline fun View.setSafeClickListener(clickInterval:Long=1500L,
                                     crossinline action: ()->Unit){
    var last=0L
    setOnClickListener {
        val gap = System.currentTimeMillis() - last
        last=System.currentTimeMillis()
        if(gap<clickInterval) return@setOnClickListener
        action.invoke()
    }
}