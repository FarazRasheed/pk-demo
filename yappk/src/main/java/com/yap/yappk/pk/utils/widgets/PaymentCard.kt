package com.yap.yappk.pk.utils.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateMarginsRelative
import com.yap.yappk.R
import com.yap.yappk.databinding.LayoutPaymentCardBinding

@SuppressLint("ResourceAsColor")
class PaymentCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs, R.attr.itemCardTitleStyle), View.OnClickListener {
    private var viewBinding: LayoutPaymentCardBinding? = null
    private var cardInfoIcon: Drawable? = null
    private var cardLogonIcon: Drawable? = null
    private var cardBackground: Drawable? = null
    private var cardName: String? = null
    private var cardNumber: String? = null
    private var cardDate: String? = null
    private var cardInfoIconVisible: Boolean = true
    private var cardChipIconVisible: Boolean = true
    private var cardInfoIconResource: Int = -1
    private var cardLogonIconResource: Int = -1
    private var cardBackgroundResource: Int = -1
    private var mCardBackgroundColor: Int = -1
    private var cardNameStyle: Int = -1
    private var cardNumberStyle: Int = -1
    private var cardSize: Int = 0
    private var cardDateStyle: Int = -1
    private var cardChipIconWidth: Int = -1
    private var cardInfoIconWidth: Int = -1
    private var cardChipIconHeight: Int = -1
    private var cardInfoIconHeight: Int = -1
    private var listener: OnPaymentCardClickListener? = null
    private var smallDp: Int = 0
    private var xSmallDp: Int = 0
    private var normalDp: Int = 0
    private var twoDp: Int = 0

    private fun initializeBinding() {
        viewBinding = LayoutPaymentCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    init {
        initializeBinding()
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.PaymentCard, 0, 0)
            cardInfoIcon = typedArray.getDrawable(
                R.styleable.PaymentCard_paymentCardInfoIcon
            )
            cardLogonIcon = typedArray.getDrawable(
                R.styleable.PaymentCard_paymentCardLogonIcon
            )
            cardBackground = typedArray.getDrawable(
                R.styleable.PaymentCard_paymentCardBackground
            )
            mCardBackgroundColor = typedArray.getColor(
                R.styleable.PaymentCard_paymentCardBackgroundColor,
                -1
            )
            cardName = typedArray.getString(
                R.styleable.PaymentCard_paymentCardName
            )
            cardNumber = typedArray.getString(
                R.styleable.PaymentCard_paymentCardNumber
            )
            cardDate = typedArray.getString(
                R.styleable.PaymentCard_paymentCardDate
            )
            cardInfoIconResource = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardInfoIconResource,
                -1
            )
            cardSize = typedArray.getInt(
                R.styleable.PaymentCard_paymentCardSize,
                0
            )
            cardLogonIconResource = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardLogonIconResource,
                -1
            )
            cardBackgroundResource = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardBackgroundResource,
                -1
            )
            cardNameStyle = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardNameStyle,
                -1
            )
            cardNumberStyle = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardNumberStyle,
                -1
            )
            cardDateStyle = typedArray.getResourceId(
                R.styleable.PaymentCard_paymentCardDateStyle,
                -1
            )
            cardChipIconWidth = typedArray.getDimensionPixelSize(
                R.styleable.PaymentCard_paymentCardChipIconWidth,
                -1
            )
            cardInfoIconWidth = typedArray.getDimensionPixelSize(
                R.styleable.PaymentCard_paymentCardInfoIconWidth,
                -1
            )
            cardChipIconHeight = typedArray.getDimensionPixelSize(
                R.styleable.PaymentCard_paymentCardChipIconHeight,
                -1
            )
            cardInfoIconHeight = typedArray.getDimensionPixelSize(
                R.styleable.PaymentCard_paymentCardInfoIconHeight,
                -1
            )
            cardInfoIconVisible = typedArray.getBoolean(
                R.styleable.PaymentCard_paymentCardInfoIconVisible,
                true
            )
            cardChipIconVisible = typedArray.getBoolean(
                R.styleable.PaymentCard_paymentCardChipIconVisible,
                true
            )
        }
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        smallDp = context.applicationContext.resources.getDimension(R.dimen.pk_margin_small)
            .toInt()
        xSmallDp = context.applicationContext.resources.getDimension(R.dimen.pk_margin_extra_small)
            .toInt()
        normalDp = context.applicationContext.resources.getDimension(R.dimen.pk_margin_normal)
            .toInt()
        twoDp = context.applicationContext.resources.getDimension(R.dimen.pk_margin_two_dp)
            .toInt()
        initializeValues()
    }

    private fun initializeValues() {
        setCardNumberStyle()
        setCardNameStyle()
        setCardDateStyle()
        setCardNumberText()
        setCardNameText()
        setCardDateText()
        setInfoIconResource()
        setBackgroundResource()
        setLogoIconResource()
        setInfoIconDrawable()
        setLogoIconDrawable()
        setBackgroundDrawable()
        setChipIconVisibility()
        setInfoIconVisibility()
        setChipIconWidth()
        setInfoIconWidth()
        setChipIconHeight()
        setInfoIconHeight()
        setBackgroundColor()
        setClickListenerOnView()
        setCardSize()
    }

    var paymentCardInfoIconWidth: Int? = null
        set(value) {
            field = value
            paymentCardInfoIconWidth?.let {
                cardInfoIconWidth = it
                setInfoIconWidth()
            }
        }
    var paymentCardSize: Int? = null
        set(value) {
            field = value
            paymentCardSize?.let {
                cardSize = it
                setCardSize()
            }
        }

    private fun setCardSize() {
        when (cardSize) {
            PaymentCardSize.SMALL.size -> {
                setMarginsForSmallSize()
            }
            PaymentCardSize.XSMALL.size -> {
                setMarginsForXSmallSize()
            }
        }
    }

    private fun setMarginsForSmallSize() {
        (viewBinding?.ivChip?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = smallDp,
            top = smallDp
        )
        (viewBinding?.tvCardNumber?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = xSmallDp,
            top = xSmallDp,
            end = xSmallDp
        )

        (viewBinding?.tvCardName?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = twoDp,
            end = twoDp
        )

        (viewBinding?.ivCardLogo?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = smallDp,
            bottom = xSmallDp
        )
        (viewBinding?.tvCardDate?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            end = xSmallDp
        )
        (viewBinding?.ivInfo?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            top = xSmallDp,
            end = xSmallDp
        )
        viewBinding?.cvMain?.radius = xSmallDp.toFloat()
        viewBinding?.ivCardLogo?.layoutParams?.width = normalDp
        viewBinding?.ivCardLogo?.layoutParams?.height = normalDp

    }

    private fun setMarginsForXSmallSize() {
        (viewBinding?.ivChip?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = xSmallDp,
            top = xSmallDp
        )
        (viewBinding?.tvCardNumber?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = twoDp,
            top = smallDp,
            end = twoDp
        )
        (viewBinding?.tvCardName?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = twoDp,
            end = twoDp
        )
        (viewBinding?.ivCardLogo?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            start = xSmallDp,
            bottom = xSmallDp
        )
        (viewBinding?.tvCardDate?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            end = xSmallDp
        )
        (viewBinding?.ivInfo?.layoutParams as ConstraintLayout.LayoutParams).updateMarginsRelative(
            top = twoDp,
            end = twoDp
        )
        viewBinding?.cvMain?.radius = xSmallDp.toFloat()
        viewBinding?.ivCardLogo?.layoutParams?.width = smallDp
        viewBinding?.ivCardLogo?.layoutParams?.height = smallDp
    }


    var paymentCardInfoIconHeight: Int? = null
        set(value) {
            field = value
            paymentCardInfoIconHeight?.let {
                cardInfoIconHeight = it
                setInfoIconHeight()
            }
        }

    var paymentCardChipIconWidth: Int? = null
        set(value) {
            field = value
            paymentCardChipIconWidth?.let {
                cardChipIconWidth = it
                setChipIconWidth()
            }
        }

    var paymentCardChipIconHeight: Int? = null
        set(value) {
            field = value
            paymentCardChipIconHeight?.let {
                cardChipIconHeight = it
                setChipIconHeight()
            }
        }

    var paymentCardNumber: String? = null
        set(value) {
            field = value
            paymentCardNumber?.let {
                cardNumber = it
                setCardNumberText()
            }
        }

    var paymentCardName: String? = null
        set(value) {
            field = value
            paymentCardName?.let {
                cardName = it
                setCardNameText()
            }
        }

    var paymentCardDate: String? = null
        set(value) {
            field = value
            paymentCardDate?.let {
                cardDate = it
                setCardDateText()
            }
        }

    var paymentCardInfoIcon: Drawable? = null
        set(value) {
            field = value
            paymentCardInfoIcon?.let {
                cardInfoIcon = it
                setInfoIconDrawable()
            }
        }
    var paymentCardLogoIcon: Drawable? = null
        set(value) {
            field = value
            paymentCardLogoIcon?.let {
                cardLogonIcon = it
                setLogoIconDrawable()
            }
        }
    var paymentCardBackground: Drawable? = null
        set(value) {
            field = value
            paymentCardBackground?.let {
                cardBackground = it
                setBackgroundDrawable()
            }
        }

    var paymentCardBackgroundColor: Int? = null
        set(value) {
            field = value
            paymentCardBackgroundColor?.let {
                mCardBackgroundColor = it
                setBackgroundColor()
            }
        }
    var paymentCardInfoIconResource: Int? = null
        set(value) {
            field = value
            paymentCardInfoIconResource?.let {
                cardInfoIconResource = it
                setInfoIconResource()
            }
        }
    var paymentCardLogonIconResource: Int? = null
        set(value) {
            field = value
            paymentCardLogonIconResource?.let {
                cardLogonIconResource = it
                setLogoIconResource()
            }
        }

    var paymentCardBackgroundResource: Int? = null
        set(value) {
            field = value
            paymentCardBackgroundResource?.let {
                cardBackgroundResource = it
                setBackgroundResource()
            }
        }

    var paymentCardInfoIconVisible: Boolean? = null
        set(value) {
            field = value
            paymentCardInfoIconVisible?.let {
                cardInfoIconVisible = it
                setInfoIconVisibility()
            }
        }

    var paymentCardChipIconVisible: Boolean? = null
        set(value) {
            field = value
            paymentCardChipIconVisible?.let {
                cardChipIconVisible = it
                setChipIconVisibility()
            }
        }

    private fun setInfoIconHeight() {
        if (cardInfoIconWidth != -1)
            viewBinding?.ivInfo?.layoutParams?.height = cardInfoIconWidth
    }

    private fun setInfoIconWidth() {
        if (cardInfoIconWidth != -1)
            viewBinding?.ivInfo?.layoutParams?.width = cardInfoIconWidth
    }

    private fun setChipIconHeight() {
        if (cardChipIconHeight != -1)
            viewBinding?.ivChip?.layoutParams?.height = cardChipIconHeight
    }

    private fun setChipIconWidth() {
        if (cardChipIconWidth != -1)
            viewBinding?.ivChip?.layoutParams?.width = cardChipIconWidth
    }

    private fun setChipIconVisibility() {
        viewBinding?.ivChip?.visibility = if (cardChipIconVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun setInfoIconVisibility() {
        viewBinding?.ivInfo?.visibility = if (cardInfoIconVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun setCardNumberStyle() {
        if (cardNumberStyle == -1) return
        viewBinding?.tvCardNumber?.setTextAppearance(cardNumberStyle)
    }

    private fun setCardNameStyle() {
        if (cardNameStyle == -1) return
        viewBinding?.tvCardName?.setTextAppearance(cardNameStyle)
    }

    private fun setCardDateStyle() {
        if (cardDateStyle == -1) return
        viewBinding?.tvCardDate?.setTextAppearance(cardDateStyle)
    }

    private fun setCardNumberText() {
        cardNumber?.let {
            viewBinding?.tvCardNumber?.text = it
        }
    }

    private fun setCardNameText() {
        cardName?.let {
            viewBinding?.tvCardName?.text = it
        }
    }

    private fun setCardDateText() {
        cardDate?.let {
            viewBinding?.tvCardDate?.text = it
        }
    }

    private fun setInfoIconResource() {
        if (cardInfoIconResource == -1) return
        viewBinding?.ivInfo?.setImageResource(cardInfoIconResource)
    }

    private fun setLogoIconResource() {
        if (cardLogonIconResource == -1) return
        viewBinding?.ivCardLogo?.setImageResource(cardLogonIconResource)
    }

    private fun setBackgroundResource() {
        if (cardBackgroundResource == -1) return
        viewBinding?.cvMain?.setBackgroundResource(cardBackgroundResource)
    }

    private fun setInfoIconDrawable() {
        cardInfoIcon?.let {
            viewBinding?.ivInfo?.setImageDrawable(it)
        }
    }

    private fun setLogoIconDrawable() {
        cardLogonIcon?.let {
            viewBinding?.ivCardLogo?.setImageDrawable(it)
        }
    }

    private fun setBackgroundDrawable() {
        cardBackground?.let {
            viewBinding?.cvMain?.setBackground(it)
        }
    }

    private fun setBackgroundColor() {
        if (mCardBackgroundColor != -1)
            viewBinding?.cvMain?.setCardBackgroundColor(mCardBackgroundColor)

    }

    private fun setClickListenerOnView() {
        viewBinding?.ivInfo?.setOnClickListener(this)
        viewBinding?.cvMain?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            viewBinding?.ivInfo -> {
                listener?.onInfoIconClicked(this)
            }
            viewBinding?.cvMain -> {
                listener?.onCardClicked(this)
            }
        }
    }

    fun setOnPaymentCardClickListener(listener: OnPaymentCardClickListener) {
        this.listener = listener
    }

    interface OnPaymentCardClickListener {
        fun onInfoIconClicked(view: View) = Unit
        fun onCardClicked(view: View) = Unit
    }

    enum class PaymentCardSize(val size: Int) {
        LARGE(0),
        SMALL(1),
        XSMALL(2)
    }
}
