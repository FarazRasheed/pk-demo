package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.swipe

interface SwipeControllerActions {
    fun onLeftClicked(position: Int) = Unit

    fun onRightClicked(position: Int) = Unit
}