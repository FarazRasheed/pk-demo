package com.yap.yappk.pk.ui.dashboard.cards.pageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yap.yappk.networking.microservices.cards.responsedtos.Card
import com.yap.yappk.pk.ui.dashboard.cards.carditem.CardItemFragment
import com.yap.yappk.pk.ui.dashboard.cards.carditem.addcard.AddCardFragment
import java.util.*


class CardPagerAdapter(
    fragment: FragmentActivity,
    private val cardsList: ArrayList<Card>,
    private val callBack: CardPagerAdapterListener,
    private val isAccountCreated: Boolean
) :
    FragmentStateAdapter(fragment) {
    private val mapFragments: HashMap<Int, Fragment> = hashMapOf()

    override fun getItemCount() =
        if (isAccountCreated) cardsList.size + 1
        else
            cardsList.size

    override fun createFragment(position: Int): Fragment {
        return when {
            cardsList.size <= position -> {
                val fragment = AddCardFragment(
                    position = position,
                    callback = callBack
                )
                mapFragments[position] = fragment
                fragment
            }
            else -> {
                val fragment = CardItemFragment(
                    card = cardsList[position],
                    position = position,
                    callback = callBack
                )
                mapFragments[position] = fragment
                fragment
            }
        }
    }

    fun getCard(position: Int): Card? {
        return when {
            cardsList.size <= position -> {
                null
            }
            else -> {
                cardsList[position]
            }
        }
    }

    fun getSize(): Int =
        cardsList.size

    fun isLastItem(position: Int): Boolean = position != 0 && cardsList.size <= position

    fun updateData(card: Card, position: Int) {
        if (position != -1) {
            cardsList[position] = card
            val fragment = mapFragments[position]
            if (fragment is CardItemFragment) {
                fragment.card = card
                fragment.notifyChangeFragment()
            }
        }
    }

}

