package com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcardpageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yap.yappk.networking.microservices.customers.responsedtos.ExternalCard
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.addexternalcarditem.AddExternalCardItemFragment
import com.yap.yappk.pk.ui.dashboard.yapit.addmoney.dashboard.carddashboard.externalcarditem.ExternalCardItemFragment
import java.util.*


class ExternalCardPagerAdapter(
    fragment: FragmentActivity,
    private val cardsList: ArrayList<ExternalCard>,
    private val callBack: ExternalCardPagerListener,
    private val isShowJustCard: Boolean = false
) :
    FragmentStateAdapter(fragment) {
    private val mapFragments: HashMap<Int, Fragment> = hashMapOf()

    override fun getItemCount() = cardsList.size + 1


    override fun createFragment(position: Int): Fragment {
        return when {
            cardsList.size <= position -> {
                val fragment = AddExternalCardItemFragment(
                    position = position,
                    callback = callBack
                )
                mapFragments[position] = fragment
                fragment
            }
            else -> {
                val fragment = ExternalCardItemFragment(
                    position = position,
                    externalCard = cardsList[position],
                    isShowJustCard = isShowJustCard,
                    callback = callBack
                )
                mapFragments[position] = fragment
                fragment
            }
        }
    }

    fun getCard(position: Int): ExternalCard? {
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

    fun updateData(card: ExternalCard, position: Int) {
        if (position != -1) {
            cardsList[position] = card
            val fragment = mapFragments[position]
//            if (fragment is CardItemFragment) {
//                fragment.card = card
//                fragment.notifyChangeFragment()
//            }
        }
    }

}

