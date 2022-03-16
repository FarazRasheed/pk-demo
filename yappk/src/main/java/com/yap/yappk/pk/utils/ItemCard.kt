package com.yap.yappk.pk.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutItemCardBinding

class ItemCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs, R.attr.itemCardTitleStyle), View.OnClickListener {
    private var viewBinding: LayoutItemCardBinding? = null
    private var itemDrawable: Drawable? = null
    private var itemCardBackgroundColor: Int? = null
    private var titleText: String? = null
    private var itemResource: Int = -1
    private var itemTitleStyle: Int = -1
    private var listener: OnCardItemClickListener? = null


    private fun initializeBinding() {
        viewBinding = LayoutItemCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }


    init {
        initializeBinding()
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.ItemCard, 0, 0)
            itemDrawable = typedArray.getDrawable(
                R.styleable.ItemCard_itemCardIcon
            )
            titleText = typedArray.getString(
                R.styleable.ItemCard_itemCardTitle
            )
            itemResource = typedArray.getResourceId(
                R.styleable.ItemCard_itemCardIconResource,
                -1
            )
            itemTitleStyle = typedArray.getResourceId(
                R.styleable.ItemCard_itemCardTitleStyle,
                -1
            )
        }
        initializeValues()
    }

    private fun initializeValues() {
        setTitleStyle()
        setTitleText()
        setIconResource()
        setIconDrawable()
        setClickListenerOnView()
    }

    private fun setTitleStyle() {
        if (itemTitleStyle == -1) return
        viewBinding?.tvTitle?.setTextAppearance(itemTitleStyle)
    }

    var itemCardTitle: String? = null
        set(value) {
            field = value
            itemCardTitle?.let {
                titleText = it
                setTitleText()
            }
        }

    var itemCardIcon: Drawable? = null
        set(value) {
            field = value
            itemCardIcon?.let {
                itemDrawable = it
                setIconDrawable()
            }
        }

    var itemCardBackground: Int? = null
        set(value) {
            field = value
            itemCardBackground?.let {
                itemCardBackgroundColor = it
                setBackgroundColor()
            }
        }

    var itemCardIconResource: Int? = null
        set(value) {
            field = value
            itemCardIconResource?.let {
                itemResource = it
                setIconResource()
            }
        }

    private fun setTitleText() {
        titleText?.let {
            viewBinding?.tvTitle?.text = it
        }
    }

    private fun setIconResource() {
        if (itemResource == -1) return
        viewBinding?.ivIcon?.setImageResource(itemResource)
    }

    private fun setIconDrawable() {
        itemDrawable?.let {
            viewBinding?.ivIcon?.setImageDrawable(it)
        }
    }

    private fun setBackgroundColor() {
        itemCardBackgroundColor?.let {
            viewBinding?.clItemMain?.setBackgroundResource(it)
        }
    }

    private fun setClickListenerOnView() {
        viewBinding?.ivIcon?.setOnClickListener(this)
        viewBinding?.tvTitle?.setOnClickListener(this)
        viewBinding?.clItemMain?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            viewBinding?.ivIcon -> {
                listener?.onItemIconClicked(this)
            }
            viewBinding?.tvTitle -> {
                listener?.onItemTitleClicked(this)
            }
            viewBinding?.clItemMain -> {
                listener?.onItemClicked(this)
            }
        }
    }

    fun setOnItemCardClickListener(listener: OnCardItemClickListener) {
        this.listener = listener
    }

    interface OnCardItemClickListener {
        fun onItemIconClicked(view: View) = Unit
        fun onItemTitleClicked(view: View) = Unit
        fun onItemClicked(view: View) = Unit
    }

}
