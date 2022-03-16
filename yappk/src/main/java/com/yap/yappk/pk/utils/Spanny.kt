package com.yap.yappk.pk.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan

class Spanny : SpannableStringBuilder {
    private var flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

    constructor() : super("")
    constructor(text: CharSequence?) : super(text)
    constructor(text: CharSequence?, vararg spans: Any?) : super(text) {
        for (span in spans) {
            setSpan(span, 0, length)
        }
    }

    constructor(text: CharSequence, span: Any?) : super(text) {
        setSpan(span, 0, text.length)
    }

    fun append(text: CharSequence?, vararg spans: Any?): Spanny {
        if (null != text) {
            append(text)
            for (span in spans) {
                setSpan(span, length - text.length, length)
            }
        }
        return this
    }

    fun append(text: CharSequence?, span: Any?): Spanny {
        if (null != text) {
            append(text)
            setSpan(span, length - text.length, length)
        }
        return this
    }

    /**
     * 添加图片
     * @return this `Spanny`.
     */
    fun append(text: CharSequence, imageSpan: ImageSpan?): Spanny {
        var text = text
        text = ".$text"
        append(text)
        setSpan(imageSpan, length - text.length, length - text.length + 1)
        return this
    }

    /**
     * 添加一个纯文本
     * @return this `Spanny`.
     */
    override fun append(text: CharSequence?): Spanny {
        text?.let { super.append(text) }
        return this
    }

    /**
     * Change the flag. Default is SPAN_EXCLUSIVE_EXCLUSIVE.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range
     * @param flag see [Spanned].
     */
    fun setFlag(flag: Int) {
        this.flag = flag
    }

    /**
     * Mark the specified range of text with the specified object.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range.
     */
    fun setSpan(span: Any?, start: Int, end: Int) {
        span?.let { setSpan(it, start, end, flag) }
    }

    /**
     * Sets a span object to all appearances of specified text in the spannable.
     * A new instance of a span object must be provided for each iteration
     * because it can't be reused.
     *
     * @param textToSpan Case-sensitive text to span in the current spannable.
     * @param getSpan    Interface to get a span for each spanned string.
     * @return `Spanny`.
     */
    fun findAndSpan(textToSpan: CharSequence, getSpan: GetSpan): Spanny {
        var lastIndex = 0
        while (lastIndex != -1) {
            lastIndex = toString().indexOf(textToSpan.toString(), lastIndex)
            if (lastIndex != -1) {
                setSpan(getSpan.span, lastIndex, lastIndex + textToSpan.length)
                lastIndex += textToSpan.length
            }
        }
        return this
    }

    /**
     * Interface to return a new span object when spanning multiple parts in the text.
     */
    interface GetSpan {
        /**
         * @return A new span object should be returned.
         */
        val span: Any?
    }

    companion object {
        /**
         * Sets span objects to the text. This is more efficient than creating a new instance of Spanny
         * or SpannableStringBuilder.
         * @return `SpannableString`.
         */
        fun spanText(
            text: CharSequence,
            vararg spans: Any?
        ): SpannableString {
            val spannableString = SpannableString(text)
            for (span in spans) {
                spannableString.setSpan(
                    span,
                    0,
                    text.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return spannableString
        }

        fun spanText(text: CharSequence, span: Any?): SpannableString {
            val spannableString = SpannableString(text)
            spannableString.setSpan(
                span,
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return spannableString
        }
    }
}
