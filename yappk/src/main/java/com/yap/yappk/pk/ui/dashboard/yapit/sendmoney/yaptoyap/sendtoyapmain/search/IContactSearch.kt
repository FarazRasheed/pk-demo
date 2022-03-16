package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search

import com.digitify.core.base.interfaces.IBase

interface IContactSearch {
    interface View : IBase.View<ViewModel> {
        var pagerSelectedPosition: Int
        fun viewModelObservers()
        fun initSearchViewListener()
    }

    interface ViewModel : IBase.ViewModel<State> {
        fun getTabTitle(position: Int): String
    }

    interface State : IBase.State
}