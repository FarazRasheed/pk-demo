package com.yap.yappk.pk.utils.popupwindow

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.circularreveal.CircularRevealCompat
import com.google.android.material.circularreveal.cardview.CircularRevealCardView
import com.yap.yappk.R
import kotlin.math.hypot
import kotlin.math.max

class Popup(context: Context, content: String) : RelativePopupWindow(context) {

    init {
        @SuppressLint("InflateParams")
        contentView = LayoutInflater.from(context).inflate(R.layout.pk_pop_up, null)
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tv = contentView.findViewById<TextView>(R.id.tvContent)
        tv.text = content

        // Disable default animation for circular reveal
        animationStyle = R.style.AnimationUpPopup
    }

    override fun showOnAnchor(
        anchor: View,
        vertPos: Int,
        horizPos: Int,
        x: Int,
        y: Int,
        fitInScreen: Boolean
    ) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y, fitInScreen)
        circularReveal(anchor)
    }

    private fun circularReveal(anchor: View) {
        (contentView as CircularRevealCardView).run {
            post {
                val myLocation = IntArray(2).apply { getLocationOnScreen(this) }
                val anchorLocation = IntArray(2).apply { anchor.getLocationOnScreen(this) }
                val cx = anchorLocation[0] - myLocation[0] + anchor.width / 2
                val cy = anchorLocation[1] - myLocation[1] + anchor.height / 2
                val windowRect = Rect().apply { getWindowVisibleDisplayFrame(this) }

                measure(
                    makeDropDownMeasureSpec(this@Popup.width, windowRect.width()),
                    makeDropDownMeasureSpec(this@Popup.height, windowRect.height())
                )
                val dx = max(cx, measuredWidth - cx)
                val dy = max(cy, measuredHeight - cy)
                val finalRadius = hypot(dx.toFloat(), dy.toFloat())
                CircularRevealCompat.createCircularReveal(
                    this,
                    cx.toFloat(),
                    cy.toFloat(),
                    0f,
                    finalRadius
                ).run {
                    duration = 500
                    start()
                }
            }
        }
    }

    companion object {
        private fun makeDropDownMeasureSpec(measureSpec: Int, maxSize: Int): Int {
            return View.MeasureSpec.makeMeasureSpec(
                getDropDownMeasureSpecSize(measureSpec, maxSize),
                getDropDownMeasureSpecMode(measureSpec)
            )
        }

        private fun getDropDownMeasureSpecSize(measureSpec: Int, maxSize: Int): Int {
            return when (measureSpec) {
                ViewGroup.LayoutParams.MATCH_PARENT -> maxSize
                else -> View.MeasureSpec.getSize(measureSpec)
            }
        }

        private fun getDropDownMeasureSpecMode(measureSpec: Int): Int {
            return when (measureSpec) {
                ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.UNSPECIFIED
                else -> View.MeasureSpec.EXACTLY
            }
        }
    }
}