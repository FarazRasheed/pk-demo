package com.yap.yappk.pk.utils

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar
import com.yap.uikit.utils.extensions.dimen
import com.yap.uikit.utils.extensions.toastNow
import com.yap.yappk.R


//SnackBar Activity Extensions
fun Activity?.showSnackBar(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG
) {
    val snakbar = Snackbar.make(
        this?.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.show(gravity)
}

fun Activity.showSnackBar(
    msg: String,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
): Snackbar? {
    val snakbar = Snackbar.make(
        this.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this, viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this, colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    textAppearance?.let { snackTextView.setTextAppearance(textAppearance) }
    setMaxLines(snackTextView)
    snakbar.setAction(actionText ?: "OK", clickListener)
    snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
    snakbar.config(marginTop?.let { dimen(it) } ?: 0, marginBottom?.let { dimen(it) } ?: 0)
    cancelAllSnackBar()
    snakbar.show(gravity)
    return snakbar
}

fun Activity.showSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
): Snackbar {
    val snakbar = Snackbar.make(
        this.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg.toString()),
        duration
    )
    snakbar.view.setBackgroundColor(ContextCompat.getColor(this, viewBgColor))
    snakbar.setTextColor(ContextCompat.getColor(this, colorOfMessage))
    val snackRootView = snakbar.view
    val snackTextView = snackRootView
        .findViewById<TextView>(R.id.snackbar_text)
    textAppearance?.let { snackTextView.setTextAppearance(textAppearance) }
    setMaxLines(snackTextView)
    snakbar.setAction(actionText ?: "OK", clickListener)
    snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
    snakbar.config(marginTop?.let { dimen(it) } ?: 0, marginBottom?.let { dimen(it) } ?: 0)
    cancelAllSnackBar()
    snakbar.show(gravity)
    return snakbar
}


//SnackBar Fragment Extensions

fun Fragment?.showSnackBar(
    msg: String,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
): Snackbar? {
    this?.let {
        val snakbar = Snackbar.make(
            it.requireActivity().window?.decorView?.findViewById(android.R.id.content)!!,
            validateString(msg),
            duration
        )
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it.requireContext(), viewBgColor))
        snakbar.setTextColor(ContextCompat.getColor(it.requireContext(), colorOfMessage))
        val snackRootView = snakbar.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        textAppearance?.let { snackTextView.setTextAppearance(textAppearance) }
        setMaxLines(snackTextView)
        snakbar.setAction(actionText ?: "OK", clickListener)
        snakbar.setTextColor(ContextCompat.getColor(snakbar.view.context, colorOfMessage))
        snakbar.config(marginTop?.let { margin -> dimen(margin) } ?: 0,
            marginBottom?.let { margin -> dimen(margin) } ?: 0)
        cancelAllSnackBar()
        snakbar.show(gravity)
        return snakbar
    }
    return null
}

fun Fragment?.showSnackBar(
    msg: String,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG
) {
    val snakbar = Snackbar.make(
        this?.requireActivity()?.window?.decorView?.findViewById(android.R.id.content)!!,
        validateString(msg),
        duration
    )
    snakbar.show(gravity)
}

fun Fragment?.showSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int? = null,
    @ColorRes colorOfMessage: Int? = null,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence? = null,
    clickListener: View.OnClickListener?
): Snackbar? {
    this?.let {
        val snakbar = Snackbar.make(
            it.requireActivity().window?.decorView?.findViewById(android.R.id.content)!!,
            validateString(msg.toString()),
            duration
        )
        viewBgColor?.let { color ->
            snakbar.view.setBackgroundColor(
                ContextCompat.getColor(
                    it.requireContext(),
                    color
                )
            )
        }
        colorOfMessage?.let { color ->
            snakbar.setTextColor(
                ContextCompat.getColor(
                    it.requireContext(),
                    color
                )
            )
        }
        val snackRootView = snakbar.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        textAppearance?.let { snackTextView.setTextAppearance(textAppearance) }
        setMaxLines(snackTextView)
        actionText?.let { snakbar.setAction(actionText ?: "OK", clickListener) }
        snakbar.config(marginTop?.let { margin -> dimen(margin) } ?: 0,
            marginBottom?.let { margin -> dimen(margin) } ?: 0)
        cancelAllSnackBar()
        snakbar.show(gravity)
        return snakbar
    }
    return null
}

fun Context?.showSnackBar(msg: String) {
    if (this is Activity) {
        showSnackBar(msg)
    } else {
        toastNow(msg)
    }
}


fun Context?.showSnackBar(
    msg: String,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
) {
    this?.let {
        if (it is Activity) {
            (it as Activity).showSnackBar(
                msg,
                viewBgColor,
                colorOfMessage,
                textAppearance,
                marginTop,
                marginBottom,
                gravity,
                duration, actionText,
                clickListener
            )
        } else {
            toastNow(msg)
        }
    }
}

fun Context?.showSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
) {
    this?.let {
        if (it is Activity) {
            (it as Activity).showSnackBar(
                msg,
                viewBgColor,
                colorOfMessage,
                textAppearance,
                marginTop,
                marginBottom,
                gravity,
                duration, actionText,
                clickListener
            )
        } else {
            toastNow(validateString(msg.toString()))
        }
    }
}

fun Snackbar?.updateSnackBarText(msg: String) {
    this?.let {
        val snackRootView = this.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.text = validateString(msg)
        setMaxLines(snackTextView)
    }
}

fun Snackbar?.updateSnackBarText(msg: CharSequence) {
    this?.let {
        val snackRootView = this.view
        val snackTextView = snackRootView
            .findViewById<TextView>(R.id.snackbar_text)
        snackTextView.text = msg
    }
}

private fun Snackbar.show(gravity: Int = Gravity.BOTTOM, addToQueue: Boolean = true) {
    val view = this.view

    when (view.layoutParams) {
        is CoordinatorLayout.LayoutParams -> {
            val param = view.layoutParams as CoordinatorLayout.LayoutParams
            param.gravity = gravity
            view.layoutParams = param
        }
        is LinearLayout.LayoutParams -> {
            val param = view.layoutParams as LinearLayout.LayoutParams
            param.gravity = gravity
        }
        else -> {
            val param = view.layoutParams as FrameLayout.LayoutParams
            param.gravity = gravity
            view.layoutParams = param
        }
    }

    if (view.layoutParams is CoordinatorLayout.LayoutParams) {
        val param = view.layoutParams as CoordinatorLayout.LayoutParams
        param.gravity = gravity
        view.layoutParams = param
    }
    this.animationMode = ANIMATION_MODE_FADE
//    val fadeIn = AlphaAnimation(0f, 1f)
//    fadeIn.interpolator = DecelerateInterpolator() //add this
//    fadeIn.duration = 1000
//
//    val fadeOut = AlphaAnimation(1f, 0f)
//    fadeOut.interpolator = AccelerateInterpolator() //and this
//    fadeOut.startOffset = 1000
//    fadeOut.duration = 1000
//
//    val animation = AnimationSet(false) //change to false
//    animation.addAnimation(fadeIn)
//    animation.addAnimation(fadeOut)
//    view.animation = animation
    if (addToQueue) {
        SnackBarQueue.snackBarQueue.add(this)
    }
    this.addCallback(object : Snackbar.Callback() {
        override fun onShown(sb: Snackbar?) {
            super.onShown(sb)
        }

        override fun onDismissed(sb: Snackbar?, event: Int) {
            super.onDismissed(sb, event)
            // SnackBarQueue.snackBarQueue.remove(sb)

        }
    })
    this.show()
}

fun cancelAllSnackBar() =
    SnackBarQueue.cancelSnackBars()

fun Snackbar?.removeSnackBar() = this?.let { SnackBarQueue.removeSnackBar(it) }

private fun Snackbar.config(
    marginTop: Int = this.view.context.dimen(android.R.attr.actionBarSize),
    marginBottom: Int = 0
) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(0, marginTop, 0, marginBottom)
    this.view.layoutParams = params
    ViewCompat.setElevation(this.view, 6f)
}


fun Fragment?.showTextUpdatedAbleSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int?=null,
    @ColorRes colorOfMessage: Int?=null,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.TOP,
    duration: Int = Snackbar.LENGTH_INDEFINITE, actionText: CharSequence? = null,
    clickListener: View.OnClickListener? = null
) {
    this?.let {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(msg)
            }
        } ?: showSnackBar(
            msg,
            viewBgColor,
            colorOfMessage,
            textAppearance,
            marginTop,
            marginBottom,
            gravity,
            duration,
            actionText,
            clickListener
        )
    }
}

fun Activity?.showTextUpdatedAbleSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence?,
    clickListener: View.OnClickListener?
) {
    this?.let {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(msg)
            }
        } ?: showSnackBar(
            msg,
            viewBgColor,
            colorOfMessage,
            textAppearance,
            marginTop,
            marginBottom,
            gravity,
            duration,
            actionText,
            clickListener
        )
    }
}

fun Context?.showTextUpdatedAbleSnackBar(
    msg: CharSequence,
    @ColorRes viewBgColor: Int,
    @ColorRes colorOfMessage: Int,
    @StyleRes textAppearance: Int? = null,
    @DimenRes marginTop: Int? = null,
    @DimenRes marginBottom: Int? = null,
    gravity: Int = Gravity.BOTTOM,
    duration: Int = Snackbar.LENGTH_LONG, actionText: CharSequence? = "OK",
    clickListener: View.OnClickListener? = null
) {
    this?.let {
        getSnackBarFromQueue(0)?.let {
            if (it.isShown) {
                it.updateSnackBarText(msg)
            }
        } ?: showSnackBar(
            msg,
            viewBgColor,
            colorOfMessage,
            textAppearance,
            marginTop,
            marginBottom,
            gravity,
            duration,
            actionText,
            clickListener
        )

    }
}


fun validateString(msg: String?): String {
    return msg ?: "null"
}


fun getSnackBarQueue() = SnackBarQueue.snackBarQueue

fun getSnackBarFromQueue(index: Int): Snackbar? {
    return if (getSnackBarQueue().size > 0) {
        SnackBarQueue.snackBarQueue[index]
    } else
        null

}

private fun setMaxLines(snackTextView: TextView) {
    snackTextView.maxLines = 5
}

private object SnackBarQueue {
    val snackBarQueue = mutableListOf<Snackbar>()

    fun cancelSnackBars() {
        snackBarQueue.forEach { it.dismiss() }
        snackBarQueue.clear()
    }

    fun removeSnackBar(snackBar: Snackbar) = snackBarQueue.remove(snackBar)

}

internal class NoSwipeBehavior : BaseTransientBottomBar.Behavior() {
    override fun canSwipeDismissView(child: View): Boolean {
        return false
    }
}

fun Context.actionBarSize(): Int {
    val styledAttributes: TypedArray = theme.obtainStyledAttributes(
        intArrayOf(
            android.R.attr.actionBarSize
        )
    )
    val mActionBarSize = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return mActionBarSize
}

fun View?.showCustomViewSnackBar(
    msg: String,
    @ColorRes viewBgColor: Int,
    @LayoutRes layoutId: Int,
    @IdRes messageTextViewId: Int,
    @IdRes actionTextViewId: Int,
    @StyleRes messageTextStyle: Int,
    @StyleRes actionTextStyle: Int,
    actionText: CharSequence,
    duration: Int = Snackbar.LENGTH_LONG,
    gravity: Int = Gravity.BOTTOM,
    clickListener: View.OnClickListener
) {
    this?.let {
        val snakbar = Snackbar.make(
            it,
            "",
            duration
        )
        val layout = snakbar.view as Snackbar.SnackbarLayout
        val layoutInflater: LayoutInflater = LayoutInflater.from(it.context)
        val snackView = layoutInflater.inflate(layoutId, null)
        layout.addView(snackView, 0)
        snakbar.behavior = NoSwipeBehavior()
        snakbar.view.setBackgroundColor(ContextCompat.getColor(it.context, viewBgColor))
        val tvMessage = layout.findViewById(messageTextViewId) as TextView
        tvMessage.text = validateString(msg)
        val tvAction = layout.findViewById(actionTextViewId) as TextView
        tvAction.text = actionText
        tvMessage.setTextAppearance(messageTextStyle)
        tvAction.setTextAppearance(actionTextStyle)
        tvAction.setOnClickListener(clickListener)
        snakbar.show(gravity)
    }

}