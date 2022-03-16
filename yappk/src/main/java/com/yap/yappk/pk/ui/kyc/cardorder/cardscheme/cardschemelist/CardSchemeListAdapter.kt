package com.yap.yappk.pk.ui.kyc.cardorder.cardscheme.cardschemelist

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseBindingRecyclerAdapter
import com.digitify.core.base.interfaces.OnItemClickListener
import com.yap.yappk.R
import com.yap.yappk.databinding.PkLayoutCardSchemeItemBinding
import com.yap.yappk.localization.*
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme
import com.yap.yappk.pk.ui.kyc.enums.CardSchemeEnum
import com.yap.yappk.pk.utils.toFormattedCurrency

class CardSchemeListAdapter(
    private val list: MutableList<CardScheme>,
    private val currency: String
) : BaseBindingRecyclerAdapter<CardScheme, CardSchemeListAdapter.CardSchemeViewHolder>(
    list
) {
    override fun onCreateViewHolder(binding: ViewDataBinding): CardSchemeViewHolder {
        return CardSchemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardSchemeViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.onBind(list[position], position, onItemClickListener)
    }


    override fun getLayoutIdForViewType(viewType: Int): Int = R.layout.pk_layout_card_scheme_item

    override fun getItemCount(): Int = list.size

    inner class CardSchemeViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: CardScheme,
            position: Int,
            onItemClickListener: OnItemClickListener?
        ) {
            when (binding) {
                is PkLayoutCardSchemeItemBinding -> {
                    handleCardSchemeBinding(binding, data, position, onItemClickListener)
                }
            }
            binding.executePendingBindings()
        }
    }

    private fun handleCardSchemeBinding(
        binding: PkLayoutCardSchemeItemBinding,
        data: CardScheme,
        position: Int,
        onItemClickListener: OnItemClickListener?
    ) {
        val context = binding.root.context
        binding.tvScheme.text = data.schemeName
        setViews(binding, context, data, currency)
        binding.tvCardButton.setOnClickListener {
            onItemClickListener?.onItemClick(it, data, position)
        }
    }

    private fun setViews(
        binding: PkLayoutCardSchemeItemBinding,
        context: Context,
        data: CardScheme,
        currency: String
    ) {
        when (data.schemeName) {
            CardSchemeEnum.PayPak.scheme -> {
                binding.tvScheme.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pkWhite
                    )
                )
                binding.ivCard.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.yap_paypak_card_scheme
                    )
                )
                binding.clCardMain.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.pk_bg_dark_blue_round
                    )
                setPayPakValues(binding, context, data, currency)
            }
            else -> {
                binding.tvScheme.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.pkDarkSlateBlue
                    )
                )
                binding.ivCard.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.yap_master_card_scheme
                    )
                )
                binding.clCardMain.background =
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.pk_bg_light_purple_round
                    )
                setMastercardValues(binding, context, data, currency)
            }
        }
    }

    private fun setPayPakValues(
        binding: PkLayoutCardSchemeItemBinding,
        context: Context,
        data: CardScheme,
        currency: String
    ) {
        val scheme =
            context.getString(screen_select_card_scheme_display_text_scheme_paypak)
        when {
            data.fee ?: 0 != 0 -> {
                val fee = data.fee?.toString()?.toFormattedCurrency(currency = currency) ?: "0"
                setPaidCardText(binding, context, scheme, fee)
            }
            else -> {
                setFreeCardText(binding, context, scheme)
            }
        }
    }

    private fun setMastercardValues(
        binding: PkLayoutCardSchemeItemBinding,
        context: Context,
        data: CardScheme,
        currency: String
    ) {
        val scheme =
            context.getString(screen_select_card_scheme_display_text_scheme_mastercard)
        val cardFee: Double = data.fee ?: 0.0
        when {
            cardFee > 0 -> {
                val fee = data.fee?.toString()?.toFormattedCurrency(currency = currency) ?: "0"
                setPaidCardText(binding, context, scheme, fee)
            }
            else -> {
                setFreeCardText(binding, context, scheme)
            }
        }
    }

    private fun setFreeCardText(
        binding: PkLayoutCardSchemeItemBinding,
        context: Context,
        scheme: String
    ) {
        binding.tvSchemeDescription.text =
            context.getString(
                screen_select_card_scheme_display_text_free_card_description,
                scheme
            )
        binding.tvCardButton.text =
            context.getString(screen_select_card_scheme_button_free_card_button)
    }

    private fun setPaidCardText(
        binding: PkLayoutCardSchemeItemBinding,
        context: Context,
        scheme: String,
        fee: String
    ) {
        binding.tvSchemeDescription.text =
            context.getString(
                screen_select_card_scheme_display_text_paid_card_description,
                scheme, fee
            )
        binding.tvCardButton.text =
            context.getString(screen_select_card_scheme_button_paid_card_button)
    }
}