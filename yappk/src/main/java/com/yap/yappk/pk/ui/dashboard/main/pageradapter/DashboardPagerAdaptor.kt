package com.yap.yappk.pk.ui.dashboard.main.pageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yap.yappk.pk.ui.dashboard.cards.CardsFragment
import com.yap.yappk.pk.ui.dashboard.home.HomeFragment
import com.yap.yappk.pk.ui.dashboard.more.MoreFragment
import com.yap.yappk.pk.ui.dashboard.store.StoreFragment

class DashboardPagerAdaptor(fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    private val fragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        0 to {
            HomeFragment()
        },
        1 to {
            StoreFragment()
        },
        2 to {
            CardsFragment()
        },
        3 to {
            MoreFragment()
        }
    )

    override fun getItemCount() = fragmentsCreators.size
    override fun createFragment(position: Int): Fragment {
        return fragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}