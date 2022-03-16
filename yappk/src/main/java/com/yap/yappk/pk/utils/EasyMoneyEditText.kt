package com.yap.yappk.pk.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.yap.uikit.utils.extensions.dip2px
import com.yap.uikit.utils.extensions.getDimensionsByPercentage
import com.yap.yappk.R
import java.text.DecimalFormat
import java.util.*


class EasyMoneyEditText : TextInputEditText {
    private var _currencySymbol: String? = null
    private var currency: String? = null
        set(value) {
            field = value
        }
    private var _showCurrency = false
    private var _showCommas = false
    private var _enableDecimal = false
    private var decimalDigits: Int = 2
    private var textToDisplay: CharSequence? = null
    private var mBackgroundColor: Int = 0
    private var mCornerRadius: Float = context.dip2px(10f)
    private var mStrokeWidth = context.dip2px(0f)
    private var customWidth: Int = 0
    private var customHeight: Int = 0
    private var maxLength: Int = resources.getInteger(R.integer.pkUnitsCount)
    private var decimalSize = 0f

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet?
    ) {
        // Setting Default Parameters
        _currencySymbol = Currency.getInstance(Locale.getDefault()).symbol
        _showCurrency = true
        _showCommas = true
        _enableDecimal = true

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            val attrArray =
                context.obtainStyledAttributes(attrs, R.styleable.EasyMoneyWidgets, 0, 0)
            try {
                var currnecy =
                    attrArray.getString(R.styleable.EasyMoneyWidgets_currency_symbol)
                if (currnecy == null) currnecy =
                    Currency.getInstance(Locale.getDefault()).symbol
                currnecy?.let { setCurrencySymbol(currnecy) }
                _showCurrency =
                    attrArray.getBoolean(R.styleable.EasyMoneyWidgets_show_currency, false)
                _showCommas = attrArray.getBoolean(R.styleable.EasyMoneyWidgets_show_commas, true)
                _enableDecimal = attrArray.getBoolean(R.styleable.EasyMoneyWidgets_enableDecimal, true)
                if (attrArray.hasValue(R.styleable.EasyMoneyWidgets_em_currency))
                    currency = attrArray.getString(R.styleable.EasyMoneyWidgets_em_currency)
                decimalDigits = attrArray.getInteger(
                    R.styleable.EasyMoneyWidgets_decimalDigits, 2
                )
                decimalSize = attrArray.getDimensionPixelSize(
                    R.styleable.EasyMoneyWidgets_em_decimalTextSize,
                    textSize.toInt()
                ).toFloat()
                if (attrArray.hasValue(R.styleable.EasyMoneyWidgets_android_maxLength)) {
                    maxLength = attrArray.getInteger(
                        R.styleable.EasyMoneyWidgets_android_maxLength,
                        7
                    )
                }

                mBackgroundColor =
                    attrArray.getColor(
                        R.styleable.EasyMoneyWidgets_em_setBackgroundColor,
                        context.getColor(R.color.pkGreySelected)
                    )
                customWidth = attrArray.getDimension(
                    R.styleable.EasyMoneyWidgets_em_custom_width, context.dip2px(
                        0
                    ).toFloat()
                ).toInt()
                customHeight = attrArray.getDimension(
                    R.styleable.EasyMoneyWidgets_em_custom_height, context.dip2px(
                        0
                    ).toFloat()
                ).toInt()
            } finally {
                attrArray.recycle()
            }
        }
        maxLines = 1
        inputType =
            if (_enableDecimal) InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED else InputType.TYPE_CLASS_NUMBER
        background = getShapeBackground()
        //setCursorColor(context.getColors(R.color.colorPrimary))
        gravity = Gravity.CENTER
//        @RequiresApi(Build.VERSION_CODES.O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        inputType =
            if (_enableDecimal) InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED else InputType.TYPE_CLASS_NUMBER
        setSingleLine()
        maxLines = 1
        filters = arrayOf(LengthFilter(maxLength))//arrayOf<InputFilter>(LengthFilter(units))
        // Add Text Watcher for Decimal formatting
        initTextWatchers()
    }

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return 134000.60
     *
     * @return A string of the raw value in the text field
     */
    fun getValueString(): String {
        var string = text?.toString()
        if (string?.contains(",") == true) {
            string = string.replace(",", "")
        }
        if (string?.contains(" ") == true) {
            string = string.substring(string.indexOf(" ") + 1, string.length)
        }
        return string ?: ""
    }

    private fun updateValue(text: String) {
        setText(text)
    }

    /**
     * Shows the commas in the text. (Default is shown).
     */
    fun showCommas() {
        _showCommas = true
        updateValue(text.toString())
    }

    /**
     * Hides the commas in the text. (Default is shown).
     */
    fun hideCommas() {
        _showCommas = false
        updateValue(text.toString())
    }

    fun getIntValue(value: String?): Double {
        if (!TextUtils.isEmpty(value)) {
            try {
                return value?.toDouble() ?: 0.0
            } catch (e: NumberFormatException) {
            }
        }
        return 0.0
    }

    /**
     * Get the value of the text without any commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return 134000.60
     *
     * @return A int of the raw value in the text field
     */
    fun getValueInt(): Double {
        return getIntValue(getValueString())
    }

    /**
     * Whether currency is shown in the text or not. (Default is true)
     *
     * @return true if the currency is shown otherwise false.
     */
    fun isShowCurrency(): Boolean {
        return _showCurrency
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param locale the locale of new symbol. (Defaul is Locale.US)
     */
    fun setCurrencySymbol(locale: Locale?) {
        setCurrencySymbol(Currency.getInstance(locale).symbol)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param currency the currency object of new symbol. (Defaul is Locale.US)
     */
    fun setCurrencySymbol(currency: Currency) {
        setCurrencySymbol(currency.symbol)
    }

    /**
     * Set the currency symbol for the edit text. (Default is US Dollar $).
     *
     * @param newSymbol the new symbol of currency in string
     */
    fun setCurrencySymbol(newSymbol: String) {
        _currencySymbol = newSymbol
        updateValue(text.toString())
    }

    /**
     * Shows the currency in the text. (Default is shown).
     */
    fun showCurrencySymbol() {
        setShowCurrency(true)
    }

    /**
     * Get the value of the text with formatted commas and currency.
     * For example, if the edit text value is $ 1,34,000.60 then this method will return exactly $ 1,34,000.60
     *
     * @return A string of the text value in the text field
     */
    fun getFormattedString(): String? {
        return text.toString()
    }

    /**
     * Hides the currency in the text. (Default is shown).
     */
    fun hideCurrencySymbol() {
        setShowCurrency(false)
    }

    private fun setShowCurrency(value: Boolean) {
        _showCurrency = value
        updateValue(text.toString())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateValue(text.toString())
    }

    private fun getDecoratedStringFromNumber(number: Long): String? {
        val numberPattern = "#,###,###,###"
        var decoStr = ""
        val formatter =
            DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
        if (_showCommas && !_showCurrency) formatter.applyPattern(numberPattern)
        else if (_showCommas && _showCurrency) formatter.applyPattern(
            "$_currencySymbol $numberPattern"
        )
        else if (!_showCommas && _showCurrency) formatter.applyPattern("$_currencySymbol ")
        else if (!_showCommas && !_showCurrency) {
            decoStr = number.toString() + ""
            return decoStr
        }
        decoStr = formatter.format(number)
        return decoStr
    }

    private fun initTextWatchers() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                this@EasyMoneyEditText.removeTextChangedListener(this)
                val backupString = charSequence.toString()
                try {
                    var originalString: String? = charSequence.toString()
                    val longval: Long
                    originalString = getValueString()
                    longval = originalString.toLong()
                    val formattedString = getDecoratedStringFromNumber(longval)

                    //setting text after format to EditText
                    if (getValueInt() <= 0.0) {
                        setText("")
                        textToDisplay = text.toString()
                        hint = if(_enableDecimal) setUpDifDecimal("0", "00") else "00"
                    } else {
                        textToDisplay = formattedString
                        setText(textToDisplay)
                    }
                    setSelection(text?.length ?: 0)
                } catch (nfe: java.lang.NumberFormatException) {
//                    nfe.printStackTrace();
                    // setText(backupString)
                    val valStr = getValueString()
                    if (valStr.isEmpty() || getValueInt() <= 0.0) {
                        setText("")
                        textToDisplay = text.toString()
                        hint = if(_enableDecimal) setUpDifDecimal("0", "00") else "00"
                    } else {
                        // Some decimal number
                        if (valStr.contains(".")) {
                            if (valStr.indexOf(".") == valStr.length - 1) {
                                // decimal has been currently put
                                val front = getDecoratedStringFromNumber(
                                    valStr.substring(
                                        0,
                                        valStr.length - 1
                                    ).toLong()
                                )
                                textToDisplay = "$front."
                                setText(textToDisplay)
                            } else {
                                val nums =
                                    getValueString().split("\\.".toRegex()).toTypedArray()
                                if (nums[1].length <= decimalDigits) {
                                    val front =
                                        getDecoratedStringFromNumber(nums[0].toLong())
                                    textToDisplay =
                                        setUpDifDecimal(front, nums[1]) //front + "." + nums[1]
                                    setText(textToDisplay)
                                } else {
                                    val front =
                                        getDecoratedStringFromNumber(nums[0].toLong())
                                    textToDisplay =
                                        setUpDifDecimal(front, nums[1].substring(0, decimalDigits))
                                    // front + "." + nums[1].substring(0, decimalDigits)
                                    setText(textToDisplay)
                                }
                            }
                        }
                    }
                    setSelection(text?.length ?: 0)
                }
                this@EasyMoneyEditText.addTextChangedListener(this)
            }

            override fun afterTextChanged(editable: Editable) {
                this@EasyMoneyEditText.removeTextChangedListener(this)
                if (editable.isNotEmpty())
                    setText(textToDisplay)
                setSelection(text?.length ?: 0)
                this@EasyMoneyEditText.addTextChangedListener(this)
            }
        })
    }

    private fun setUpDifDecimal(front: String?, decimal: CharSequence): Spanny {
        val result = Spanny()
        result.append(front)
            // result.append(getDecimalFormatted(integerD.intValueExact().toLong()))
            .append(".")
            .setSpan(
                AbsoluteSizeSpan(textSize.toInt(), false),
                0,
                result.length
            )
        result.append(
            decimal,
            // BigDecimal(str).intValueExact().toString(),
            AbsoluteSizeSpan(decimalSize.toInt(), false)
        )
        return result
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setEditTextDimension()
    }


    private fun setEditTextDimension() {
        val dimensions = context.getDimensionsByPercentage(50, 8)
        val params = layoutParams
        params.width = dimensions[0]
        params.height = dimensions[1]
        layoutParams = params
    }

    private fun getShapeBackground(@ColorInt color: Int = context.getColor(R.color.pkDarkSlateBlue)): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = mCornerRadius
        shape.setColor(mBackgroundColor)
        shape.setStroke(mStrokeWidth.toInt(), color)
        return shape
    }

    public override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:em_currency")
        fun setCurrency(view: EasyMoneyEditText, currency: String?) {
            currency?.let { view.currency = it }
        }
    }

}