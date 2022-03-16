package com.yap.yappk.pk.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.animation.Animation
import androidx.viewbinding.ViewBinding
import com.yap.yappk.R

fun View.startAnimation(animation: Animation, onEnd: () -> Unit) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            onEnd()
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit
    })
    this.startAnimation(animation)
}

inline fun <reified T : ViewBinding> Activity.showDialog(
    dialogVH: BaseViewHolder,
    isCancelable: Boolean,
    noinline getViewBinding: (() -> T)

): AlertDialog {
    val mViewBinding = getViewBinding()
    val builder = AlertDialog.Builder(this)
    var alertDialog: AlertDialog? = null
    val marginPx = resources.getDimension(R.dimen.pk_margin_normal_large).toInt()
    builder.setView(mViewBinding.root)
    builder.setCancelable(isCancelable)
    alertDialog = builder.create()
    dialogVH.onBind(mViewBinding)
    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    alertDialog.window?.attributes?.height = resources.getDimension(R.dimen._350sdp).toInt()
    alertDialog.window?.decorView?.setPadding(marginPx, 0, marginPx, 0)
    alertDialog.show()
    return alertDialog
}
