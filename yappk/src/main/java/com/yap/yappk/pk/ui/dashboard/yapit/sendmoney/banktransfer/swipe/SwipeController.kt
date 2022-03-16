package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.swipe

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.RecyclerView
import com.yap.yappk.R
import com.yap.yappk.localization.common_button_delete
import com.yap.yappk.localization.common_button_view
import com.yap.yappk.localization.getString
import kotlin.math.max
import kotlin.math.min


class SwipeController : ItemTouchHelper.Callback() {
    private var swipeBack = false

    private var buttonShowedState: ButtonsState =
        ButtonsState.GONE

    private var buttonLeftInstance: RectF? = null
    private var buttonRightInstance: RectF? = null

    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    private var buttonsActions: SwipeControllerActions? = null

    private val buttonWidth = 500f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    fun swipeController(buttonsActions: SwipeControllerActions) {
        this.buttonsActions = buttonsActions
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let { drawButtons(c, it) }
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var mdX = 0f
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) mdX =
                    max(dX, this.buttonWidth)
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) mdX =
                    min(dX, -this.buttonWidth)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    mdX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            } else {
                setTouchListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (dX < -this.buttonWidth) buttonShowedState =
                    ButtonsState.RIGHT_VISIBLE else if (dX > this.buttonWidth) buttonShowedState =
                    ButtonsState.LEFT_VISIBLE
                if (buttonShowedState != ButtonsState.GONE) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                super@SwipeController.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { _, _ -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                if (buttonsActions != null && buttonRightInstance != null && buttonRightInstance != null
                ) {
                    if (buttonLeftInstance?.contains(
                            event.x,
                            event.y
                        ) == true
                    ) buttonsActions?.onLeftClicked(
                        viewHolder.absoluteAdapterPosition
                    ) else if (buttonRightInstance?.contains(
                            event.x,
                            event.y
                        ) == true
                    ) {
                        buttonsActions?.onRightClicked(viewHolder.absoluteAdapterPosition)
                    }
                }
                buttonShowedState = ButtonsState.GONE
                currentItemViewHolder = null
            }
            false
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        val buttonWidthWithoutPadding: Float = this.buttonWidth / 2
        val corners = 0f
        val itemView = viewHolder.itemView
        val itemAll = viewHolder.itemView.findViewById<TextView>(R.id.tvAll)
        itemAll?.let {
            val p = Paint()
            val leftButton = RectF(
                itemView.right - buttonWidthWithoutPadding * 2,
                itemAll.bottom.toFloat(),
                itemView.right.toFloat() - buttonWidthWithoutPadding,
                itemView.bottom.toFloat()
            )
            p.color = ContextCompat.getColor(itemView.context, R.color.pkBlueWithAHintOfPurple)
            c.drawRoundRect(leftButton, corners, corners, p)
            drawText(
                itemView.context.getString(common_button_view),
                c,
                leftButton,
                p,
                itemView.context.resources.getDimension(R.dimen.pk_margin_small)
            )
            drawImage(
                ContextCompat.getDrawable(itemView.context, R.drawable.pk_ic_view),
                leftButton,
                c
            )
            val rightButton = RectF(
                itemView.right - buttonWidthWithoutPadding,
                itemAll.bottom.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat()
            )
            p.color = ContextCompat.getColor(itemView.context, R.color.pkWarning)
            c.drawRoundRect(rightButton, corners, corners, p)
            drawImage(
                ContextCompat.getDrawable(itemView.context, R.drawable.pk_ic_white_cross),
                rightButton,
                c
            )
            drawText(
                itemView.context.getString(common_button_delete),
                c,
                rightButton,
                p,
                itemView.context.resources.getDimension(R.dimen.pk_margin_small)
            )
            buttonLeftInstance = leftButton
            buttonRightInstance = rightButton
        }

    }

    private fun drawImage(drawable: Drawable?, button: RectF, c: Canvas) {
        drawable?.setBounds(
            button.left.toInt() + (button.left * 0.08).toInt(),
            button.top.toInt() + (button.top * 0.50).toInt(),
            button.right.toInt() - (button.right * 0.06).toInt(),
            button.bottom.toInt() - (button.bottom * 0.35).toInt()
        )
        drawable?.draw(c)
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint, textSize: Float) {
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize
        val textWidth = p.measureText(text)
        c.drawText(
            text,
            button.centerX() - textWidth / 2,
            button.centerY() + (textSize * 2).toInt(),
            p
        )
    }

}