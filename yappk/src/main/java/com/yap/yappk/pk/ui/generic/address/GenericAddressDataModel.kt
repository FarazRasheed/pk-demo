package com.yap.yappk.pk.ui.generic.address


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericAddressDataModel(
    var toolbarTitle: String? = "",
    var title: String? = "",
    var subTitle: String? = "",
    var buttonText: String? = ""
) :
    Parcelable
