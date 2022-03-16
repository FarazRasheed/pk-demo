package com.yap.yappk.pk.ui.dashboard.yapit.enum

sealed class ContactFragmentType (val type: Int) {
    object yapContact : ContactFragmentType(0)
    object phoneContact : ContactFragmentType(1)
}